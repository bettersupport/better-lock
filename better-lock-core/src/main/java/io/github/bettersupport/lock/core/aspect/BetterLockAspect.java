package io.github.bettersupport.lock.core.aspect;

import io.github.bettersupport.lock.core.annotation.GlobalSynchronized;
import io.github.bettersupport.lock.core.exception.GlobalLockException;
import io.github.bettersupport.lock.core.model.LockAttribute;
import io.github.bettersupport.lock.core.model.LockParam;
import io.github.bettersupport.lock.core.model.ZookeeperClient;
import io.github.bettersupport.lock.core.properties.BetterLockProperties;
import io.github.bettersupport.lock.core.support.LockInterface;
import io.github.bettersupport.lock.core.support.LockerConfig;
import io.github.bettersupport.lock.core.support.LockerFactory;
import io.github.bettersupport.lock.core.support.StackThreadLocalHandler;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * 切面
 * @author wang.wencheng
 * date 2021-7-11
 * describe
 */
@Aspect
@Component
public class BetterLockAspect {
    private static final Logger log = LoggerFactory.getLogger(BetterLockAspect.class);

    /**
     * 线程内的锁的各项参数
     */
    public static ThreadLocal<Stack<LockAttribute>> lockAttributeThreadLocal = new ThreadLocal<>();

    /**
     * 锁的配置参数
     */
    @Autowired
    private BetterLockProperties lockProperties;
    /**
     * springboot redis数据源
     */
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;
    /**
     * Redisson客户端
     */
    @Autowired(required = false)
    private RedissonClient redissonClient;
    /**
     * Zookeeper客户端
     */
    @Autowired(required = false)
    private ZookeeperClient zookeeperClient;

    @Before("@annotation(io.github.bettersupport.lock.core.annotation.GlobalSynchronized)")
    public void lockMethod(JoinPoint joinPoint) throws GlobalLockException {

        // 获取锁的各项参数，包括锁的主键和超时时间
        LockAttribute lockAttribute = getLockKeyByAnnotation(joinPoint);

        // 获取锁对象
        LockInterface locker = LockerFactory.getLocker(lockProperties.getLockType(),
                LockerConfig
                        .build()
                        .buildRedisConfig(redisTemplate)
                        .buildRedisClusterConfig(redissonClient)
                        .buildZookeeperClient(zookeeperClient));
        lockAttribute.setLocker(locker);

        StackThreadLocalHandler.set(lockAttributeThreadLocal, lockAttribute);

        if (lockAttribute.getGlobalSynchronized().lockWait()) {
            // 等待锁，一直等待知道获取到锁为止
            locker.lock(lockAttribute.getLockKey(), lockAttribute.getTimeOut());
        } else {
            // 不等待锁，直接返回获取锁的结果
            boolean lockResult = locker.lockWithoutWait(lockAttribute.getLockKey(), lockAttribute.getTimeOut());
            if (null == lockAttribute.getParam()) {
                throw new GlobalLockException("LockParam 参数不能为空");
            }
            lockAttribute.getParam().set(LockParam.lockResultKey, lockResult);
        }

        log.debug("lockMethod lockType {}, lockKey {}", lockProperties.getLockType(), lockAttribute.getLockKey());

    }

    /**
     * 方法返回之后获取锁并解锁
     * @param joinPoint 切面参数
     * @throws GlobalLockException 分布式锁异常
     */
    @After("@annotation(io.github.bettersupport.lock.core.annotation.GlobalSynchronized)")
    public void unlockMethod(JoinPoint joinPoint) throws GlobalLockException {

        LockAttribute lockAttribute = StackThreadLocalHandler.getAndRelease(lockAttributeThreadLocal);

        if (lockAttribute == null) {
            throw new GlobalLockException("ThreadLocal 不存在锁属性");
        }
        LockInterface locker = lockAttribute.getLocker();

        locker.unlock(lockAttribute.getLockKey());

        log.debug("unlockMethod lockType {}, lockKey {}", lockProperties.getLockType(), lockAttribute.getLockKey());

    }

    /**
     * 通过注解获取加锁的参数
     * @param joinPoint
     * @return
     * @throws GlobalLockException
     */
    private LockAttribute getLockKeyByAnnotation(JoinPoint joinPoint) throws GlobalLockException {

        // 获取方法的各项属性
        Class methodClass = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs();


        // 获取LockParam
        LockParam param = new LockParam();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof LockParam) {
                param = (LockParam) args[i];
            }
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        GlobalSynchronized globalSynchronized = method.getAnnotation(GlobalSynchronized.class);
        if (globalSynchronized == null) {
            throw new GlobalLockException("解析注解出现错误，注解为空。");
        }

        // 获取分布式锁主键
        String lockKey = LockInterface.lockKeyPrefix + LockInterface.lockKeyColon;

        if (StringUtils.isBlank(globalSynchronized.lockKey())) {
            String classNameLockKey = methodClass.getName().replaceAll("\\.", LockInterface.lockKeyColon);
            lockKey += classNameLockKey + LockInterface.lockKeyColon + method.getName();
        } else {
            lockKey += globalSynchronized.lockKey();

            // 获取锁主键自定义值
            String customValue;
            String customValueKey = globalSynchronized.customValueKey();
            if (StringUtils.isBlank(customValueKey)) {
                customValue = "";
            } else {
                Object customValueObject = param.get(customValueKey);
                if (customValueObject == null) {
                    customValue = "";
                } else {
                    customValue = customValueObject.toString();
                }
            }

            lockKey = String.format(lockKey, customValue);
        }

        LockAttribute lockAttribute = new LockAttribute();

        lockAttribute.setGlobalSynchronized(globalSynchronized);
        lockAttribute.setParam(param);
        lockAttribute.setLockKey(lockKey);
        lockAttribute.setTimeOut(globalSynchronized.timeOut());

        return lockAttribute;

    }


}
