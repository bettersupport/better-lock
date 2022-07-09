package io.github.bettersupport.lock.demo.manager;

import io.github.bettersupport.lock.core.annotation.GlobalSynchronized;
import io.github.bettersupport.lock.core.model.LockParam;
import io.github.bettersupport.lock.demo.dao.ImSessionTestMapper;
import io.github.bettersupport.lock.demo.model.Response;
import io.github.bettersupport.lock.demo.service.impl.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author wang.wencheng
 * @since 2022-1-15
 */
@Component
public class TestManager {
    private static final Logger log = LoggerFactory.getLogger(TestManager.class);

    @Autowired
    private TestManager testManager;
    @Autowired
    private ImSessionTestMapper imSessionTestMapper;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    @GlobalSynchronized(lockKey = "lock:test:%s", customValueKey = "sessionId", timeOut = 10000L)
    public Response<String> test(LockParam<String, String> param) {
        String sessionId = param.get("sessionId");
        if (imSessionTestMapper.countBySession(sessionId) <= 0) {
            log.info("会话ID已不存在");
            return Response.buildError("会话ID已不存在");
        }
        log.info("会话ID存在");
        long startTimestamp = System.currentTimeMillis();
        Response<String> response;
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("删除会话ID");
        log.info("deleted: {}", imSessionTestMapper.deletBySessionId(sessionId));
        response = Response.buildResult("success");
//        try {
//            Thread.sleep(2000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        long endTimestamp = System.currentTimeMillis();

        log.info("testRequest: Start: {} End: {}, param {},Started use {}ms", new Date(startTimestamp), new Date(endTimestamp), param.get("lockResult"), (endTimestamp - startTimestamp));

        return response;
    }

    public void test2() {

    }

}
