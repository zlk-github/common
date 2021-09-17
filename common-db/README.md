# common-db

### 介绍与使用

Spring Boot 如何集成 mybatis-plus，包含分页，自动注入通用字段，代码生成插件等。

mybatis-plus官网 https://mp.baomidou.com/

common-db其余配置与测试见测试项目common-test下mybatis-plus-test

* [mybatis-plus-test](https://github.com/zlk-github/common-test/blob/master/common-db-test/README.md#mybatis-plus-test)

### mysql集群（未完待续）

#### 1 主从

MySQL主从模式是指数据可以从一个MySQL数据库服务器主节点复制到一个或多个从节点。MySQL 默认采用异步复制方式。

1.1 主从复制(一主多从) 

    完全依赖于半同步复制，如果半同步复制退化为异步复制，数据一致性无法得到保证；
    需要额外考虑haproxy、keepalived的高可用机制。

1.2 MMM架构(双主多从) 是一套支持双主故障切换和双主日常管理的脚本程序，MMM使用Perl语言开发。主要用来监控和管理MySQL Master-Master(双)复制。--稳定性差

1.3 MHA架构(多主多从) MHA是基于标准的MySQL复制(异步/半同步)。

#### 2 共享存储

    一般共享存储采用比较多的是 SAN/NAS 方案。

#### 3 操作系统实时数据块复制

    这个方案的典型场景是 DRBD，DRBD架构(MySQL+DRBD+Heartbeat)

#### 4 数据库高可用架构

    这种方式比较经典的案例包括 MGR(MySQL Group Replication)和 Galera 等，最近业内也有一些类似的尝试，如使用一致性协议算法，自研高可用数据库的架构等。
    
    1.MGR(MySQL Group Replication，MySQL官方开发的一个实现MySQL高可用集群的一个工具。第一个GA版本正式发布于MySQL5.7.17中)
    
    2.Galera

#### 5 MySQL Cluster和PXC

    MySQL Cluster(ndb存储引擎，比较复杂，业界并没有大规模使用)
    
    PXC(Percona XtraDB Cluster) 强一致性

### 参考

    集群 https://www.cnblogs.com/rouqinglangzi/p/10921982.html

    主从 https://www.cnblogs.com/EasonJim/p/7635366.html