package cn.better.lock.core.model;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.StandardLockInternalsDriver;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * @author wang.wencheng
 * @date 2021-7-30
 * @remark
 */
public class ZookeeperLockDriver extends StandardLockInternalsDriver {

    private long timeOut;

    public ZookeeperLockDriver(long leaseTime, TimeUnit unit) {
        this.timeOut = unit.toMillis(leaseTime);
    }

    @Override
    public String createsTheLock(CuratorFramework client, String path, byte[] lockNodeBytes) throws Exception {
        String ourPath;
        if ( lockNodeBytes != null ) {

            ourPath = client.create().withTtl(timeOut).creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.PERSISTENT_SEQUENTIAL_WITH_TTL).forPath(path, lockNodeBytes);

        } else {

            ourPath = client.create().withTtl(timeOut).creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.PERSISTENT_SEQUENTIAL_WITH_TTL).forPath(path);

        }
        return ourPath;
    }
}
