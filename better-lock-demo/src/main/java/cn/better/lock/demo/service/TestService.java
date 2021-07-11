package cn.better.lock.demo.service;

import cn.better.lock.core.model.LockParam;
import cn.better.lock.demo.model.Response;

public interface TestService {

    Response<String> testRequest(LockParam<String, String> param);

}
