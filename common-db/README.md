# common-db

Spring Boot 如何集成 mybatis-plus,并引入逆向工程。

mybatis-plus官网 https://mp.baomidou.com/

### pom.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
    <artifactId>common</artifactId>
    <groupId>org.example</groupId>
    <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    
        <artifactId>common-db</artifactId>
    
        <dependencies>
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis.plus.version}</version>
            </dependency>
        </dependencies>
    </project>

其余配置见测试项目common-test下mybatis-plus-test

* [mybatis-plus-test](https://github.com/zlk-github/common-test/blob/master/cgfbommon-db-test/README.md#mybatis-plus-test)