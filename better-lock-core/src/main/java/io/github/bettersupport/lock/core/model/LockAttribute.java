package io.github.bettersupport.lock.core.model;

import io.github.bettersupport.lock.core.annotation.GlobalSynchronized;
import io.github.bettersupport.lock.core.support.LockInterface;

/**
 * 锁的各项参数
 * @author wang.wencheng
 * date 2021-7-13
 * describe
 */
public class LockAttribute {

    private GlobalSynchronized globalSynchronized;

    private LockInterface locker;

    private LockParam param;

    private String lockKey;

    private long timeOut;

    public GlobalSynchronized getGlobalSynchronized() {
        return globalSynchronized;
    }

    public void setGlobalSynchronized(GlobalSynchronized globalSynchronized) {
        this.globalSynchronized = globalSynchronized;
    }

    public LockInterface getLocker() {
        return locker;
    }

    public void setLocker(LockInterface locker) {
        this.locker = locker;
    }

    public LockParam getParam() {
        return param;
    }

    public void setParam(LockParam param) {
        this.param = param;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
}
