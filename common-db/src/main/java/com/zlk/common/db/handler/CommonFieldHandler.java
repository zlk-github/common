package com.zlk.common.db.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author likuan.zhou
 * @title: CommonFieldHandler
 * @projectName common
 * @description: 字段填充
 * @date 2021/9/11/011 17:12
 */
@Slf4j
@Component
public class CommonFieldHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //this.setInsertFieldValByName( "creater", getUser(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //this.setUpdateFieldValByName("updateBy", getUser(), metaObject);
    }
}
