package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.godofball.reggie.common.BaseContext;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.pojo.Orders;
import com.godofball.reggie.service.OrderService;
import javafx.scene.layout.BorderStrokeStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        log.info(orders.toString());
        orderService.submit(orders);
        return Result.success("下单成功");
    }

    @GetMapping("/userPage")
    public Result<Page> userPage(Integer page,Integer pageSize){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        orderService.page(ordersPage,ordersLambdaQueryWrapper);
        return Result.success(ordersPage);
    }

}
