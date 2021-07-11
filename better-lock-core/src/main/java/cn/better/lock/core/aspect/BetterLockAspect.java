package cn.better.lock.core.aspect;

import cn.better.lock.core.annotation.GlobalSynchronized;
import cn.better.lock.core.exception.GlobalLockException;
import cn.better.lock.core.properties.BetterLockProperties;
import com.google.common.base.Throwables;
import org.aspectj.lang.JoinPoint;
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

        try {
            Object[] args = joinPoint.getArgs();
            Class<?>[] argTypes = new Class[joinPoint.getArgs().length];

            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }

            Method method = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), argTypes);

            GlobalSynchronized globalSynchronized = method.getAnnotation(GlobalSynchronized.class);

            log.info("lockMethod lockType {}", lockProperties.getLockType());

        } catch (Exception e) {
            log.error("lockMethod:ERROR: {}", Throwables.getStackTraceAsString(e));
            throw new GlobalLockException(e);
        }
    }

}
