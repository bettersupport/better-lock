package cn.better.lock.demo.api;

import cn.better.lock.core.model.LockParam;
import cn.better.lock.demo.model.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface TestApi {

    @RequestMapping(value = "testRequest", method = RequestMethod.POST)
    Response<String> testRequest(@RequestBody LockParam<String, Object> param);

    @RequestMapping(value = "testRequest2", method = RequestMethod.POST)
    Response<String> testRequest2(@RequestBody LockParam<String, Object> param);

}
