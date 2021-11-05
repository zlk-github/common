# common-spring_security

### 介绍与使用

Spring Boot 如何集成SpringSecurity+SpringSecurity+Oauth2+JWT实现第三方登录(认证授权与资源服务)

    SpringSecurity实现认证与授权。
    OAuth2做第三方登录，提供Token做为访问权限。（单点登录，一个系统登录，相关系统无需再次登录就可以进入其他系统）
    JWT用来生成无状态Token。

    对比方案：Shiro、Sa-Token、Spring Session等。

注：不是必须三个都要配置，可以按项目需要配置即可。

spring_security官网 https://mp.baomidou.com/

common-db其余配置与测试见测试项目common-test下mybatis-plus-test

* [spring_security-test](https://github.com/zlk-github/common-test/blob/master/common-spring_security-test/README.md#spring_security-test)


### 参考

    搭建：https://blog.csdn.net/breakaway_01/article/details/108402805

    官网：https://spring.io/projects/spring-security