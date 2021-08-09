package io.github.bettersupport.lock.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 分布式全局锁注解
 * @author wang.wencheng
 * @date 2021-7-11
 * @remark
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalSynchronized {

    /**
     * 锁的key，支持format模式
     * @return
     */
    @AliasFor("lockKey")
    String value() default "";

    /**
     * 锁的key，支持format模式String.format("%s World", "Hello")
     * @return
     */
    @AliasFor("value")
    String lockKey() default "";

    /**
     * 自定义值主键，根据主键从LockParam中取值
     * @return
     */
    String customValueKey() default "";

    /**
     * 锁超时时间
     * @return
     */
    long timeOut() default 5000L;

    /**
     * 是否等待锁
     * @return
     */
    boolean lockWait() default true;

}
