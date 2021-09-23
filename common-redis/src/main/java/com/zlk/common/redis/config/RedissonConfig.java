package com.zlk.common.redis.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author likuan.zhou
 * @title: RedisConfig
 * @projectName common
 * @description: Redisson分布式锁-配置类
 * @date 2021/9/16/016 19:05
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password:}")
    private String password;

    @Bean
    public RedissonClient getRedisson(){
        Config config = new Config();
        //单机模式  依次设置redis地址和密码.其余配置未加
        config.useSingleServer().
                setAddress("redis://"+host+":"+port);
        if (StringUtils.isEmpty(password)) {
            config.useSingleServer().setPassword(null);
        } else {
            config.useSingleServer().setPassword(password);
        }
        // 主库提供写能力，然后主库复制到从库，从库提供读能力。
        //添加主从配置(一主多从，最常见一主两从三哨兵)
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
        // 集群模式配置（多主搭配多从） setScanInterval()扫描间隔时间，单位是毫秒, //可以用"rediss://"来启用SSL连接
        // config.useClusterServers().setScanInterval(2000).addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001").addNodeAddress("redis://127.0.0.1:7002");
        return Redisson.create(config);
    }


}
