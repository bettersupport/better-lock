package io.github.bettersupport.lock.core.model;

import io.github.bettersupport.lock.core.exception.GlobalLockException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author wang.wencheng
 * @date 2021-7-29
 * @remark
 */
public class ZookeeperClient {

    /**
     * zookeeper相关参数
     */
    private static final int sessionTimeout = 5000;

    private static final int connectTimeout = 5000;

    private static final int baseSleepTimeMs = 1000;

    private static final int maxRetries = 3;

    private static final int maxSleepMs = 5000;

    private CuratorFramework zkClient;

    /**
     * 构造函数
     * @param nodes zookeeper各节点
     */
    private ZookeeperClient(String nodes) {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, maxSleepMs);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(nodes)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectTimeout)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
    }

    public static ZookeeperClient createClient(String nodes) throws IOException {
        return new ZookeeperClient(nodes);
    }

    /**
     * 获取锁
     * @param lockKey 锁主键
     * @param leaseTime 失效时间
     * @param unit 时间单位
     * @return 锁对象
     * @throws GlobalLockException 分布式锁异常
     */
    public InterProcessMutex getLock(String lockKey, long leaseTime, TimeUnit unit) throws GlobalLockException {
        if (lockKey == null || lockKey.length() == 0) {
            throw new GlobalLockException("锁Key不能为空");
        }
        // 格式化锁key为zookeeper路径格式
        String key = String.format("/%s", lockKey.replaceAll(":", "/"));
        InterProcessMutex lock = new InterProcessMutex(zkClient, key, new ZookeeperLockDriver(leaseTime, unit));
        return lock;
    }
}
