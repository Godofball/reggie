package com.godofball.reggie.interceptor;

import com.alibaba.fastjson2.JSON;
import com.godofball.reggie.common.BaseContext;
import com.godofball.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long empId = (Long) request.getSession().getAttribute("employee");
        Long userId = (Long) request.getSession().getAttribute("user");
        if(empId==null&&userId==null){
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            log.info("拦截请求成功：{}",request.getRequestURL());
            return false;
        }
        log.info("拦截请求失败：{}",request.getRequestURL());
        if(empId==null){
            BaseContext.setCurrentId(userId);
        }
        if(userId==null){
            BaseContext.setCurrentId(empId);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
        BaseContext.removeCurrentId();
    }
}
