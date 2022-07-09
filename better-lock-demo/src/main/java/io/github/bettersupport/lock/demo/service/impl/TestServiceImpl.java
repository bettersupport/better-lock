package io.github.bettersupport.lock.demo.service.impl;

import io.github.bettersupport.lock.core.annotation.GlobalSynchronized;
import io.github.bettersupport.lock.core.model.LockParam;
import io.github.bettersupport.lock.demo.dao.ImSessionTestMapper;
import io.github.bettersupport.lock.demo.manager.TestManager;
import io.github.bettersupport.lock.demo.model.Response;
import io.github.bettersupport.lock.demo.model.TestHandler;
import io.github.bettersupport.lock.demo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TestServiceImpl implements TestService {
    private static final Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private TestManager testManager;


    @Override

    public Response<String> testRequest(LockParam<String, String> param, Long suId) {
        return testManager.test(param);
    }

    @Override
    @GlobalSynchronized(lockKey = "lock:test", timeOut = 10000L)
    public Response<String> testRequest2(LockParam<String, String> param) {
        long startTimestamp = System.currentTimeMillis();

        try {
            Thread.sleep(10000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTimestamp = System.currentTimeMillis();

        log.info("testRequest2: Start: {} End: {}, param {},Started use {}s", new Date(startTimestamp), new Date(endTimestamp), param.getLockResult(), (endTimestamp - startTimestamp)/1000);

        return Response.buildResult("success");
    }
}
