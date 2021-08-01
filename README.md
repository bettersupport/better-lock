# better-lock
分布式锁

### 使用zookeeper分布式锁时请开启zookeeper超时节点配置如下
```bash
# zoo.cfg 开启支持超时节点的配置
extendedTypesEnabled=true

# zkServer.sh(.cmd)启动脚本新增参数，检查超时节点的时间间隔1s(1000ms)
"-Dznode.container.checkIntervalMs=1000"
```

### 使用方法
#### maven 引入
```xml
    <dependency>
        <groupId>cn.better.lock</groupId>
        <artifactId>better-lock-core</artifactId>
        <version>${version}</version>
    </dependency>
```

#### yml配置
```yaml
# zookeeper单节点
spring:
  better:
    lock:
      lock-type: zookeeper
      zookeeper:
        nodes: 127.0.0.1:2181
```
```yaml
# zookeeper多节点
spring:
  better:
    lock:
      lock-type: zookeeper
      zookeeper:
        nodes: 127.0.0.1:2181,127.0.0.1:2182
```
```yaml
# springboot redis
spring:
  better:
    lock:
      lock-type: redis
```
```yaml
# redisson 根据spring.redis配置自动选择单节点，集群和哨兵模式
spring:
  better:
    lock:
      lock-type: redisson
```

#### 注解
```java
class LockTest{
    /**
     * 注解字段意义
     * lockKey String 锁Key，支持格式 test:lock:%s
     * timeOut Long 锁超时时间，单位毫秒 默认{@code 5000L}
     * customValueKey String 自定义值的主键，用于从入参LockParam中获取自定义参数来格式化 lockKey
     *                例如 test:lock:%s  customValueKey对应值为 better，则最后lockKey为test:lock:better
     * lockWait Boolean 是否等待获取锁 默认{@code true}
     */
    @GlobalSynchronized(lockKey = "lock:test", timeOut = 10000L)
    public void lockTest(LockParam<String, Object> param) {
        try {
            Thread.sleep(10000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Hello World");
    }
}
```