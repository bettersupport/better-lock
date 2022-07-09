package io.github.bettersupport.lock.demo.dao;

/**
 * @author wang.wencheng
 * @since 2022-6-23
 */
public interface ImSessionTestMapper {

    int countBySession(String sessionId);

    int insertSelective(String sessionId);

    int deletBySessionId(String sessionId);

}
