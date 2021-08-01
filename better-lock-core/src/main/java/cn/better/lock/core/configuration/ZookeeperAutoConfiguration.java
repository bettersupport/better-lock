package cn.better.lock.core.configuration;

import cn.better.lock.core.model.ZookeeperClient;
import cn.better.lock.core.properties.BetterLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * Zookeeper的配置类
 * @author wang.wencheng
 * @date 2021-7-28
 * @remark
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.better.lock", name = "lock-type", havingValue = BetterLockProperties.LockType.ZOOKEEPER_LOCK)
public class ZookeeperAutoConfiguration {

    @Autowired
    private BetterLockProperties betterLockProperties;

    /**
     * 创建ZookeeperCLient
     * @return
     * @throws IOException
     */
    @Bean
    public ZookeeperClient zookeeperClient() throws IOException {
        // 创建ZookeeperCLient
        return ZookeeperClient.createClient(betterLockProperties.getZookeeper().getNodes());
    }
}
