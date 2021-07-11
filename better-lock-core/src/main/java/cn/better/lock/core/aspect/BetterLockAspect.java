package cn.better.lock.core.aspect;

import cn.better.lock.core.annotation.GlobalSynchronized;
import cn.better.lock.core.exception.GlobalLockException;
import cn.better.lock.core.model.LockParam;
import cn.better.lock.core.properties.BetterLockProperties;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

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

            LockParam param = new LockParam();

            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
                if (args[i] instanceof LockParam) {
                    param = (LockParam) args[i];
                }
            }

            Method method = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), argTypes);

            GlobalSynchronized globalSynchronized = method.getAnnotation(GlobalSynchronized.class);
            if (globalSynchronized == null) {
                throw new GlobalLockException("解析注解出现错误，注解为空。");
            }

            String customValue;
            String customValueKey = globalSynchronized.customValueKey();
            if (StringUtils.isBlank(customValueKey)) {
                customValue = "";
            } else {
                customValue = (String) param.get(customValueKey);
            }

            String lockKey = String.format(globalSynchronized.lockKey(), customValue);

            log.info("lockMethod lockType {}", lockKey);

        } catch (Exception e) {
            log.error("lockMethod:ERROR: {}", Throwables.getStackTraceAsString(e));
            throw new GlobalLockException(e);
        }
    }

}
