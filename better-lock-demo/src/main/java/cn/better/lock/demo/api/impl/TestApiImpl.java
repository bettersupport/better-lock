package cn.better.lock.demo.api.impl;

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
    public Response<String> testRequest(String param) {
        return testService.testRequest(param);
    }
}
