package io.github.bettersupport.lock.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置参数
 * @author wang.wencheng
 * date 2021-7-11
 * describe
 */
@ConfigurationProperties(prefix = "spring.better.lock")
public class BetterLockProperties {

    /**
     * 分布式锁类型，取值范围{@link LockType}
     */
    private String lockType = LockType.REDIS_LOCK;

    /**
     * zookeeper配置{@link Zookeeper}
     */
    private Zookeeper zookeeper;

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public static class LockType {
        public static final String REDIS_LOCK = "redis";
        public static final String REDISSON_LOCK = "redisson";
        public static final String ZOOKEEPER_LOCK = "zookeeper";
    }

    /**
     * zookeeper配置
     */
    public static class Zookeeper {

        /**
         * 节点 127.0.0.1：2181,127.0.0.1：2182
         */
        private String nodes;

        public String getNodes() {
            return nodes;
        }

        public void setNodes(String nodes) {
            this.nodes = nodes;
        }
    }

}
