package io.github.bettersupport.lock.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 被锁方法的入参
 * @author wang.wencheng
 * date 2021-7-11
 * describe
 * @param <K> KEY
 * @param <V> VALUE
 */
public class LockParam<K,V> extends HashMap<K,V> {

    /**
     * 不等待锁获取锁结果的 Key
     */
    public static final String lockResultKey = "lockResult";

    public LockParam(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public LockParam(int initialCapacity) {
        super(initialCapacity);
    }

    public LockParam() {
    }

    public LockParam(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public V set(K key, V value) {
        return put(key, value);
    }

    /**
     * 获取不等待锁的结果
     * @return
     */
    public V getLockResult() {
        return this.get(lockResultKey);
    }
}
