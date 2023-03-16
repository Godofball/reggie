package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.dto.SetmealDto;
import com.godofball.reggie.pojo.Category;
import com.godofball.reggie.pojo.Setmeal;
import com.godofball.reggie.pojo.SetmealDish;
import com.godofball.reggie.service.CategoryService;
import com.godofball.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveSetmealDto(setmealDto);
        return Result.success("套餐添加成功");
    }

    @GetMapping("page")
    public Result<Page> page(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        Page<Setmeal> setmealPage = new Page<>();

        //查询条件
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.hasLength(name),Setmeal::getName,name);//按名称查找
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);//按更新时间查找
        setmealService.page(setmealPage,setmealLambdaQueryWrapper);


        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> setmeals = setmealPage.getRecords();
        List<SetmealDto> setmealDtos=new LinkedList<>();

        setmeals.forEach(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            //讲setmeal对象内容移到setmealDto
            BeanUtils.copyProperties(setmeal,setmealDto);
            //查找套装分类并给每个setmealDto的categoryName属性赋值
            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                setmealDto.setCategoryName(category.getName());
            }
            setmealDtos.add(setmealDto);
        });

        setmealDtoPage.setRecords(setmealDtos);
        return Result.success(setmealDtoPage);
    }

    @DeleteMapping
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids){
        log.info(ids.toString());
        setmealService.removeSetmealDtoBatch(ids);
        return Result.success("套餐删除成功");
    }

    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealById(@PathVariable("id") Long id){
        log.info("id={}",id);
        SetmealDto setmealDto = setmealService.getSetmealDtoById(id);
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result<String> updateSetmealDto(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.updateSetmealDto(setmealDto);
        return Result.success("套餐修改成功");
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list( Setmeal setmeal){
        log.info(setmeal.toString());

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmeals = setmealService.list(setmealLambdaQueryWrapper);

        return Result.success(setmeals);

    }

}