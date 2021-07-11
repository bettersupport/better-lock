package cn.better.lock.demo.api;

import cn.better.lock.demo.model.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface TestApi {

    @RequestMapping(value = "testRequest", method = RequestMethod.POST)
    Response<String> testRequest(@RequestParam String param);

}
