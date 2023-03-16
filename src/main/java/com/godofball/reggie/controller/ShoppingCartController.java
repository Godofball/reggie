package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.godofball.reggie.common.BaseContext;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.pojo.ShoppingCart;
import com.godofball.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("list")
    public Result<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return Result.success(shoppingCarts);
    }

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shoppingCartInfo = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (shoppingCartInfo!=null){
            Integer number = shoppingCartInfo.getNumber();
            shoppingCartInfo.setNumber(number+1);
            shoppingCartService.updateById(shoppingCartInfo);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }

        return Result.success(shoppingCart);
    }

    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,ShoppingCart::getUserId,userId);
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        shoppingCart = shoppingCartService.getOne(queryWrapper);

        if (shoppingCart!=null&&shoppingCart.getNumber()-1>1){
            shoppingCart.setNumber(shoppingCart.getNumber()-1);
            shoppingCartService.updateById(shoppingCart);
        }else {
            shoppingCartService.remove(queryWrapper);
        }

        return Result.success("操作成功");
    }

    @DeleteMapping("/clean")
    public Result<String> clean(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(queryWrapper);
        return Result.success("操作成功");
    }
}
