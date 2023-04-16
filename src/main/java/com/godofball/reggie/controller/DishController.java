package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.dto.DishDto;
import com.godofball.reggie.pojo.Category;
import com.godofball.reggie.pojo.Dish;
import com.godofball.reggie.pojo.DishFlavor;
import com.godofball.reggie.service.CategoryService;
import com.godofball.reggie.service.DishFlavorService;
import com.godofball.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        dishService.saveDishAndFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    @GetMapping("/page")
    public Result<Page> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort);
        dishService.page(dishPage, queryWrapper);//根据名字和顺序查找

        Page<DishDto> dishDtoPage = new Page<>();
        //将dishPage的内容拷贝到dishDtoPage，除了records之外
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> dishes = dishPage.getRecords();
        LinkedList<DishDto> dishDtos = new LinkedList<>();
        dishes.forEach(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            if (category!=null) {
                dishDto.setCategoryName(category.getName());
            }
            dishDtos.add(dishDto);
        });
        dishDtoPage.setRecords(dishDtos);
        return Result.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public Result<DishDto> getDishById(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getDishDtoById(id);
        return Result.success(dishDto);
    }

    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        dishService.updateDishAndFlavor(dishDto);
        return Result.success("修改菜品成功");
    }

    @DeleteMapping
    public Result<String> deleteBatch(@RequestParam List<Long> ids){
        log.info("ids={}",ids.toString());

        dishService.deleteDishAndFlavorWithBatch(ids);
        return Result.success("删除菜品成功");
    }

    @PostMapping("/status/{status}")
    public Result<String> updateStatusByIds(@PathVariable Integer status,@RequestParam List<Long> ids){
        log.info("status={},ids={}",status,ids);

        Set keys = redisTemplate.keys("dish*");
        keys.forEach(key->redisTemplate.delete(key));

        dishService.updateStatusByBatch(status,ids);
        return Result.success("修改成功");
    }


    @GetMapping("/list")
    public Result<List<DishDto>> getDishByCategory(Dish dish){
        List<DishDto> dishDtos=null;

        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtos!=null){
            return Result.success(dishDtos);
        }

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.like(StringUtils.hasLength(dish.getName()),Dish::getName,dish.getName());
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);

        dishDtos = new LinkedList<>();
        List<DishDto> finalDishDtos = dishDtos;
        dishes.forEach(dishItem->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishItem,dishDto);
            Long dishId = dishDto.getId();

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(flavors);

            finalDishDtos.add(dishDto);

        });

        redisTemplate.opsForValue().set(key,dishDtos,60, TimeUnit.MINUTES);

        return Result.success(dishDtos);
    }

}
