package io.github.bettersupport.lock.demo.service;

import io.github.bettersupport.lock.core.model.LockParam;
import io.github.bettersupport.lock.demo.model.Response;

public interface TestService {

    Response<String> testRequest(LockParam<String, String> param, Long suId);

    Response<String> testRequest2(LockParam<String, String> param);

}
