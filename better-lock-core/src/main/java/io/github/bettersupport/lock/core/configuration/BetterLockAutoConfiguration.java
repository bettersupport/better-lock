package io.github.bettersupport.lock.core.configuration;

import io.github.bettersupport.lock.core.properties.BetterLockProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 切面的自动配置
 * @author wang.wencheng
 * date 2021-7-11
 * describe
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"cn.better.lock.core.aspect"})
@EnableConfigurationProperties(BetterLockProperties.class)
public class BetterLockAutoConfiguration {
}
