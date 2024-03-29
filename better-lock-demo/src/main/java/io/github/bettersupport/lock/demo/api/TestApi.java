package io.github.bettersupport.lock.demo.api;

import io.github.bettersupport.lock.core.model.LockParam;
import io.github.bettersupport.lock.demo.model.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface TestApi {

    @RequestMapping(value = "testRequest", method = RequestMethod.POST)
    Response<String> testRequest(@RequestBody LockParam<String, String> param);

    @RequestMapping(value = "testRequest2", method = RequestMethod.POST)
    Response<String> testRequest2(@RequestBody LockParam<String, String> param);

}
