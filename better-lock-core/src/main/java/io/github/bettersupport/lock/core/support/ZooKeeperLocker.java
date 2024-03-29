package io.github.bettersupport.lock.core.support;

import io.github.bettersupport.lock.core.exception.GlobalLockException;
import io.github.bettersupport.lock.core.model.ZookeeperClient;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper锁
 * @author wang.wencheng
 * date 2021-7-13
 * describe
 */
public class ZooKeeperLocker implements LockInterface{

    private static ThreadLocal<Stack<InterProcessMutex>> zLockThreadLocal = new ThreadLocal<>();

    private ZookeeperClient zookeeperClient;

    public ZooKeeperLocker(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    @Override
    public void lock(String lockKey, long leaseTime) throws GlobalLockException {
        try {
            InterProcessMutex zLock = zookeeperClient.getLock(lockKey, leaseTime, TimeUnit.MILLISECONDS);
            StackThreadLocalHandler.set(zLockThreadLocal, zLock);
            zLock.acquire();
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public boolean lockWithoutWait(String lockKey, long leaseTime) throws GlobalLockException {
        try {
            InterProcessMutex zLock = zookeeperClient.getLock(lockKey, leaseTime, TimeUnit.MILLISECONDS);
            StackThreadLocalHandler.set(zLockThreadLocal, zLock);
            return zLock.acquire(0, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {
        try {
            InterProcessMutex zLock = StackThreadLocalHandler.getAndRelease(zLockThreadLocal);;
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
