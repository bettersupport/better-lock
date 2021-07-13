package cn.better.lock.core.model;

import cn.better.lock.core.annotation.GlobalSynchronized;

public class LockAttribute {

    private GlobalSynchronized globalSynchronized;

    private String lockKey;

    private long timeOut;

    public GlobalSynchronized getGlobalSynchronized() {
        return globalSynchronized;
    }

    public void setGlobalSynchronized(GlobalSynchronized globalSynchronized) {
        this.globalSynchronized = globalSynchronized;
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
