package cn.better.lock.core.model;

import java.util.HashMap;
import java.util.Map;

public class LockParam<K,V> extends HashMap<K,V> {

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
}
