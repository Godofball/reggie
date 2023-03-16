package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.godofball.reggie.common.BaseContext;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.pojo.User;
import com.godofball.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session){
        String phone = map.get("phone").toString();
        log.info("phone={}",phone);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(userLambdaQueryWrapper);
        if (user==null){
            user=new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user",user.getId());
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session){
        session.removeAttribute("userId");
        return Result.success("退出成功");
    }
}
