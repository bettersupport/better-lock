package cn.better.lock.demo.api.impl;

import cn.better.lock.core.model.LockParam;
import cn.better.lock.demo.api.TestApi;
import cn.better.lock.demo.model.Response;
import cn.better.lock.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiImpl implements TestApi {

    @Autowired
    private TestService testService;

    @Override
    public Response<String> testRequest(LockParam<String, Object> param) {
        return testService.testRequest(param);
    }

    @Override
    public Response<String> testRequest2(LockParam<String, Object> param) {
        return testService.testRequest2(param);
    }
}
