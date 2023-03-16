package com.godofball.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * MybatisPlus自动填充公共字段
 */
@Slf4j
@Component
public class MyMateObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充");
        Date date = new Date();
        if (metaObject.hasSetter("createTime")) metaObject.setValue("createTime",date);
        if (metaObject.hasSetter("updateTime")) metaObject.setValue("updateTime",date);
        if (metaObject.hasSetter("createUser")) metaObject.setValue("createUser",BaseContext.getCurrentId());
        if (metaObject.hasSetter("updateUser")) metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.info("设置createTime，createUser；{}",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充");
        if (metaObject.hasSetter("updateTime")) metaObject.setValue("updateTime",new Date());
        if (metaObject.hasSetter("updateUser")) metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.info("设置updateTime，updateUser；{}",BaseContext.getCurrentId());
    }

}
