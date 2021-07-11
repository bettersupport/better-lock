package cn.better.lock.demo.service.impl;

import cn.better.lock.core.annotation.GlobalSynchronized;
import cn.better.lock.core.model.LockParam;
import cn.better.lock.core.properties.BetterLockProperties;
import cn.better.lock.demo.model.Response;
import cn.better.lock.demo.service.TestService;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestServiceImpl implements TestService {
    private static final Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private BetterLockProperties betterLockProperties;

    @Override
    @GlobalSynchronized(lockKey = "lock:test:%s", customValueKey = "testKey")
    public Response<String> testRequest(LockParam<String, String> param) {
        long startTimestamp = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("", Throwables.getStackTraceAsString(e));
        }

        long endTimestamp = System.currentTimeMillis();

        log.info("{} Start: {} End: {}, Started {} use {}s", betterLockProperties.getLockType(), new Date(startTimestamp), new Date(endTimestamp), param, (endTimestamp - startTimestamp)/1000);

        return Response.buildResult(param.get("testKey"));
    }
}
