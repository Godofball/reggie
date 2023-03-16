package com.godofball.reggie.common;

import lombok.extern.slf4j.Slf4j;

/**
 * 公共类，用来保存和获取当前的Employee对象id
 */
@Slf4j
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        log.info("设置当前线程id：{}",id);
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
    public static void removeCurrentId(){
        log.info("移除当前线程id");
        threadLocal.remove();
    }

}
