package com.godofball.reggie.common;

import com.godofball.reggie.exception.CustomException;
import com.godofball.reggie.exception.DishException;
import com.godofball.reggie.exception.SetmealException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 公共类，用来处理全局异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info("异常信息：{}",ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            return Result.error(s[2]+"已存在");
        }
        return Result.error("操作失败");
    }

    @ExceptionHandler(DishException.class)
    public Result<String> exceptionHandler(DishException ex){
        log.info("异常信息：{}",ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(SetmealException.class)
    public Result<String> exceptionHandler(SetmealException ex){
        log.info("异常信息：{}",ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex){
        log.info("异常信息：{}",ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
