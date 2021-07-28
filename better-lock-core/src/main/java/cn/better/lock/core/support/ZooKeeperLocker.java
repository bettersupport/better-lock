package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;
import cn.better.lock.core.properties.BetterLockProperties;

public class ZooKeeperLocker implements LockInterface{

    private BetterLockProperties betterLockProperties;

    public ZooKeeperLocker(BetterLockProperties betterLockProperties) {
        this.betterLockProperties = betterLockProperties;
    }

    @Override
    public void lock(String lockKey, long timeOut) throws GlobalLockException {
        System.out.println(betterLockProperties.getZookeeper().getNodes());
    }

    @Override
    public boolean lockWithoutWait(String lockKey, long timeOut) throws GlobalLockException {
        return true;
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {

    }

}
