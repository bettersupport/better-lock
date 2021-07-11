package cn.better.lock.demo.service;

import cn.better.lock.demo.model.Response;

public interface TestService {

    Response<String> testRequest(String param);

}
