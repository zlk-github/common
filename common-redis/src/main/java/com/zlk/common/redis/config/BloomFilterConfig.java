package com.zlk.common.redis.config;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.zlk.common.redis.bloom.BloomFilterHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author likuan.zhou
 * @title: RedisConfig
 * @projectName common
 * @description: 布隆过滤器配置类
 * @date 2021/10/18/016 19:05
 */
@Configuration
public class BloomFilterConfig {

    //初始化布隆过滤器，放入到spring容器里面
    @Bean
    public BloomFilterHelper<String> initBloomFilterHelper() {
        return new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.01);
    }

}
