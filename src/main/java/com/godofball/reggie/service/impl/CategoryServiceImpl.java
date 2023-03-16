package com.godofball.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godofball.reggie.exception.DishException;
import com.godofball.reggie.exception.SetmealException;
import com.godofball.reggie.mapper.CategoryMapper;
import com.godofball.reggie.pojo.Category;
import com.godofball.reggie.pojo.Dish;
import com.godofball.reggie.pojo.Setmeal;
import com.godofball.reggie.service.CategoryService;
import com.godofball.reggie.service.DishService;
import com.godofball.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);//根据分类查找菜品
        if (dishService.count(dishQueryWrapper)>0){//判断关联菜品数量
            throw new DishException("分类已关联菜品");
        }
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        if(setmealService.count(setmealQueryWrapper)>0){
            throw new SetmealException("分类已关联套餐");
        }
        super.removeById(id);
    }
}
