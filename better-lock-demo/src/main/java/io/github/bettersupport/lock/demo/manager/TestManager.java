package io.github.bettersupport.lock.demo.manager;

import io.github.bettersupport.lock.core.annotation.GlobalSynchronized;
import org.springframework.stereotype.Component;

/**
 * @author wang.wencheng
 * @since 2022-1-15
 */
@Component
public class TestManager {

    @GlobalSynchronized(lockKey = "lock:test:manager", timeOut = 1000L)
    public void test() {

    }

}
