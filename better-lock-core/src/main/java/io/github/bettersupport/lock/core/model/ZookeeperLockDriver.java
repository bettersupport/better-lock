package io.github.bettersupport.lock.core.model;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.StandardLockInternalsDriver;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * zookeeper 获取锁驱动
 * @author wang.wencheng
 * date 2021-7-30
 * describe
 */
public class ZookeeperLockDriver extends StandardLockInternalsDriver {

    private long ttl;

    public ZookeeperLockDriver(long leaseTime, TimeUnit unit) {
        this.ttl = unit.toMillis(leaseTime);
    }

    @Override
    public String createsTheLock(CuratorFramework client, String path, byte[] lockNodeBytes) throws Exception {
        String ourPath;
        if ( lockNodeBytes != null ) {

            ourPath = client.create().withTtl(ttl).creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.PERSISTENT_SEQUENTIAL_WITH_TTL).forPath(path, lockNodeBytes);

        } else {

            ourPath = client.create().withTtl(ttl).creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.PERSISTENT_SEQUENTIAL_WITH_TTL).forPath(path);

        }
        return ourPath;
    }
}
