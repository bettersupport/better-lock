package cn.better.lock.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

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
     * 锁的key，支持format模式
     * @return
     */
    @AliasFor("value")
    String lockKey() default "";

    /**
     * 自定义值主键
     * @return
     */
    String customValueKey() default "";

    /**
     * 锁超时时间
     * @return
     */
    long timeOut() default 5000L;

}
