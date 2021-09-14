package com.zlk.common.db.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author likuan.zhou
 * @title: MybatisPlusConfig
 * @projectName common
 * @description: mybatis-plus分页
 * @date 2021/9/11/011 17:05
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {
    /**
     * 分页配置
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

}
