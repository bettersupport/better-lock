package cn.better.lock.core.config;

import cn.better.lock.core.properties.BetterLockProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BetterLockProperties.class)
public class BetterLockAutoConfiguration {
}
