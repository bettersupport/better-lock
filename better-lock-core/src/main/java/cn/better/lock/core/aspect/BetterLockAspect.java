package cn.better.lock.core.aspect;

import cn.better.lock.core.annotation.GlobalSynchronized;
import cn.better.lock.core.exception.GlobalLockException;
import cn.better.lock.core.model.LockParam;
import cn.better.lock.core.properties.BetterLockProperties;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class BetterLockAspect {
    private static final Logger log = LoggerFactory.getLogger(BetterLockAspect.class);

    @Autowired
    private BetterLockProperties lockProperties;

    @Before("@annotation(cn.better.lock.core.annotation.GlobalSynchronized)")
    public void lockMethod(JoinPoint joinPoint) throws GlobalLockException {

        String lockKey = getLockKeyByAnnotation(joinPoint);

        log.info("lockMethod lockType {}", lockKey);

    }

    @After("@annotation(cn.better.lock.core.annotation.GlobalSynchronized)")
    public void unlockMethod(JoinPoint joinPoint) throws GlobalLockException {

        String lockKey = getLockKeyByAnnotation(joinPoint);

        log.info("unlockMethod lockType {}", lockKey);

    }


    private String getLockKeyByAnnotation(JoinPoint joinPoint) throws GlobalLockException {

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

        return lockKey;

    }


}
