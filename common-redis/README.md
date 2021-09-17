# common-redis

### 介绍与使用

Spring Boot 如何集成redis做缓存(默认过期时间),分布式锁，布隆表达式，消息传递/发布订阅, Redis 事务。

redis官网 https://www.redis.net.cn/tutorial/3501.html

文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

common-redis其余配置与测试见测试项目common-test下common-redis-test

* [common-redis-test](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README.md#common-redis-test)


#### redis的5种数据类型：

    string 字符串（可以为整形、浮点型和字符串，统称为元素）
    list 列表（实现队列,元素不唯一，先入先出原则）
    set 集合（各不相同的元素）
    hash hash散列值（hash的key必须是唯一的）
    sort set 有序集合

### Redis 集群（未完待续）

#### 1 主从-哨兵模式

一主两从三哨兵。能满足高可用，但是选举时会间断。扩展难，有性能瓶颈。

#### 2 集群模式

最少3个master节点，且每个主节点下挂一个slave节点。

### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html