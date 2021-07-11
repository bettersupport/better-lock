package cn.better.lock.core.config;

import cn.better.lock.core.properties.BetterLockProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"cn.better.lock.core.aspect"})
@EnableConfigurationProperties(BetterLockProperties.class)
public class BetterLockAutoConfiguration {
}
