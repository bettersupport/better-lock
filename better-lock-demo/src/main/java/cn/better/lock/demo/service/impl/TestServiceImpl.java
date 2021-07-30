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


    @Override
    @GlobalSynchronized(lockKey = "lock:test", timeOut = 10000L)
    public Response<String> testRequest(LockParam<String, Object> param) {
        long startTimestamp = System.currentTimeMillis();

        try {
            Thread.sleep(10000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTimestamp = System.currentTimeMillis();

        log.info("testRequest: Start: {} End: {}, param {},Started use {}ms", new Date(startTimestamp), new Date(endTimestamp), param.get("lockResult"), (endTimestamp - startTimestamp));

        return Response.buildResult("success");
    }

    @Override
    @GlobalSynchronized(lockKey = "lock:test", timeOut = 10000L)
    public Response<String> testRequest2(LockParam<String, Object> param) {
        long startTimestamp = System.currentTimeMillis();

        long endTimestamp = System.currentTimeMillis();

        log.info("testRequest2: Start: {} End: {}, param {},Started use {}s", new Date(startTimestamp), new Date(endTimestamp), param.getLockResult(), (endTimestamp - startTimestamp)/1000);

        return Response.buildResult("success");
    }
}
