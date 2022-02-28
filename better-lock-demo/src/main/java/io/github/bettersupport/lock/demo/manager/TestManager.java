package io.github.bettersupport.lock.demo.manager;

import io.github.bettersupport.lock.core.annotation.GlobalSynchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wang.wencheng
 * @since 2022-1-15
 */
@Component
public class TestManager {

    @Autowired
    private TestManager testManager;

    @GlobalSynchronized(lockKey = "lock:test:manager", timeOut = 1000L)
    public void test() {
        testManager.test2();
    }

    @GlobalSynchronized(lockKey = "lock:test:manager2", timeOut = 1000L)
    public void test2() {

    }

}
