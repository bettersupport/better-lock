package cn.better.lock.demo.service;

import cn.better.lock.core.model.LockParam;
import cn.better.lock.demo.model.Response;

public interface TestService {

    Response<String> testRequest(LockParam<String, Object> param);

    Response<String> testRequest2(LockParam<String, Object> param);

}
