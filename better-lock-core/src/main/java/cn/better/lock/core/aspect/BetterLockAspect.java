package cn.better.lock.core.aspect;

import cn.better.lock.core.annotation.GlobalSynchronized;
import cn.better.lock.core.exception.GlobalLockException;
import cn.better.lock.core.model.LockAttribute;
import cn.better.lock.core.model.LockParam;
import cn.better.lock.core.properties.BetterLockProperties;
import cn.better.lock.core.support.LockInterface;
import cn.better.lock.core.support.LockerConfig;
import cn.better.lock.core.support.LockerFactory;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class BetterLockAspect {
    private static final Logger log = LoggerFactory.getLogger(BetterLockAspect.class);

    public static ThreadLocal<LockAttribute> lockAttributeThreadLocal = new ThreadLocal<>();

    @Autowired
    private BetterLockProperties lockProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Before("@annotation(cn.better.lock.core.annotation.GlobalSynchronized)")
    public void lockMethod(JoinPoint joinPoint) throws GlobalLockException {

        LockAttribute lockAttribute = getLockKeyByAnnotation(joinPoint);

        lockAttributeThreadLocal.set(lockAttribute);

        LockInterface locker = LockerFactory.getLocker(lockProperties.getLockType(),
                LockerConfig.build().buildRedisConfig(redisTemplate));
        locker.lock(lockAttribute.getLockKey(), lockAttribute.getTimeOut());

        log.debug("lockMethod lockType {}, lockKey {}", lockProperties.getLockType(), lockAttribute.getLockKey());

    }

    @After("@annotation(cn.better.lock.core.annotation.GlobalSynchronized)")
    public void unlockMethod(JoinPoint joinPoint) throws GlobalLockException {

        LockAttribute lockAttribute = lockAttributeThreadLocal.get();

        if (lockAttribute == null) {
            throw new GlobalLockException("ThreadLocal 不存在锁属性");
        }
        // 释放ThreadLocal
        lockAttributeThreadLocal.set(null);

        LockInterface locker = LockerFactory.getLocker(lockProperties.getLockType(),
                LockerConfig.build().buildRedisConfig(redisTemplate));
        locker.unlock(lockAttribute.getLockKey());

        log.debug("unlockMethod lockType {}, lockKey {}", lockProperties.getLockType(), lockAttribute.getLockKey());

    }


    private LockAttribute getLockKeyByAnnotation(JoinPoint joinPoint) throws GlobalLockException {

        Class methodClass = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs();
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];


        LockParam param = new LockParam();

        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
            if (args[i] instanceof LockParam) {
                param = (LockParam) args[i];
            }
        }

        Method method;
        try {
            method = methodClass.getMethod(joinPoint.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            throw new GlobalLockException(e);
        }

        GlobalSynchronized globalSynchronized = method.getAnnotation(GlobalSynchronized.class);
        if (globalSynchronized == null) {
            throw new GlobalLockException("解析注解出现错误，注解为空。");
        }

        // 获取分布式锁主键
        String lockKey;

        if (StringUtils.isBlank(globalSynchronized.lockKey())) {
            String classNameLockKey = methodClass.getName().replaceAll("\\.", ":");
            lockKey = "betterLock:" + classNameLockKey + ":" + method.getName();
        } else {
            lockKey = globalSynchronized.lockKey();

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
        lockAttribute.setLockKey(lockKey);
        lockAttribute.setTimeOut(globalSynchronized.timeOut());

        return lockAttribute;

    }


}
