# common-redis

### 介绍与使用

Redis是C实现的。各版本的差异？为什么性能这么好？

Spring Boot 如何集成redis做缓存(默认过期时间),分布式锁，布隆表达式，消息传递/发布订阅, Redis 事务，redis持久化等。

**common-redis其余配置与测试见测试项目**common-test下common-redis-test

* [common-redis-test](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README.md#common-redis-test)


#### redis的5种数据类型：

    string 字符串（可以为整形、浮点型和字符串，统称为元素） --String字符串或者json字符串，常规下使用最多，存放字符串。
           string 类型的值最大能存储 512MB。常用命令：get、set、incr、decr、mget等。

    list 列表（实现队列,元素不唯一，先入先出原则）  -- 类比java中LinkedList，比如twitter的关注列表，粉丝列表等都可以用Redis的list结构来实现。
         双向链表实现，可以用作栈与队列。内存开销稍大，更新删除等容易，离链表两端远查询会稍慢。
         常用命令：lpush（添加左边元素）,rpush,lpop（移除左边第一个元素）,rpop,lrange（获取列表片段，LRANGE key start stop）等。

    set 集合（各不相同的元素） -- 类比java中HashSet，redis中set集合是通过hashtable实现的，没有顺序，适合存放需要去重的数据。
        常用命令：sadd,spop,smembers,sunion 等。

    hash hash散列值（hash的key必须是唯一的） -- 类比java中HashMap (list转换后的Map; 一般为key:map<item,Object>,其中item为Object对象主键) ，
        适合存放转换后对象列表（通过id查询对应数据，如用户信息）。常用命令：hget,hset,hgetall 等。
   
    sort set 有序集合  -- 类比java中HashSet，但是有分值比重，适合做去重的排行榜等热数据。
         常用命令：zadd,zrange,zrem,zcard等

    另：范围查询，Bitmaps，Hyperloglogs 和地理空间（Geospatial）索引半径查询

### Redis 集群（未完待续）

详见：https://github.com/zlk-github/common-test/blob/master/common-redis-test/README.md

#### 1 主从-哨兵模式

一主两从三哨兵。能满足高可用，但是选举时会间断。扩展难，有性能瓶颈。

#### 2 集群模式

最少3个master节点，且每个主节点下挂一个slave节点。

### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    redis源码地址：https://github.com/redis/redis
    
    springboot redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556