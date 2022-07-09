package io.github.bettersupport.lock.demo.configure;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "io.github.bettersupport.lock.demo.dao", sqlSessionFactoryRef = "lockSqlSessionFactory")
public class LockDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.lock")
    public DataSource lockDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /*下面是本地事务*/

    @Bean
    public SqlSessionFactory lockSqlSessionFactory(@Qualifier("lockDataSource")DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/lock/*.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager lockTransactionManager(@Qualifier("lockDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate lockSqlSessionTemplate(
            @Qualifier("lockSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
