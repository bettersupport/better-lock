# better-lock
分布式锁

### 使用zookeeper分布式锁时请开启zookeeper超时节点配置如下
```bash
# zoo.cfg 开启支持超时节点的配置
extendedTypesEnabled=true

# zkServer.sh(.cmd)启动脚本新增参数，检查超时节点的时间间隔1s(1000ms)
"-Dznode.container.checkIntervalMs=1000"
```