package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;
import cn.better.lock.core.model.ZookeeperClient;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public class ZooKeeperLocker implements LockInterface{

    private static ThreadLocal<InterProcessMutex> zLockThreadLocal = new ThreadLocal<>();

    private ZookeeperClient zookeeperClient;

    public ZooKeeperLocker(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    @Override
    public void lock(String lockKey, long timeOut) throws GlobalLockException {
        try {
            InterProcessMutex zLock = zookeeperClient.getLock(lockKey);
            zLockThreadLocal.set(zLock);
            zLock.acquire();
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public boolean lockWithoutWait(String lockKey, long timeOut) throws GlobalLockException {
        try {
            InterProcessMutex zLock = zookeeperClient.getLock(lockKey);
            zLockThreadLocal.set(zLock);
            return zLock.acquire(0, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {
        try {
            InterProcessMutex zLock = zLockThreadLocal.get();
            zLockThreadLocal.set(null);
            if (zLock.isAcquiredInThisProcess() && zLock.isOwnedByCurrentThread()) {
                try {
                    zLock.release();
                } catch (IllegalMonitorStateException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

}
