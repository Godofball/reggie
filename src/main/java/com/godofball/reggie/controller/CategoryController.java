package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.pojo.Category;
import com.godofball.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public Result<String> add(@RequestBody Category category) {
        log.info("category:{}",category);
        categoryService.save(category);
        return Result.success("分类添加成功");
    }

    @GetMapping("/page")
    public Result<Page> page(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("page={},pageSize={}",page,pageSize);
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> deleteCategory(Long id){
        categoryService.remove(id);
        return Result.success("删除成功");
    }

    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category){
        log.info("update category:{}",category);
        categoryService.updateById(category);
        return Result.success("分类修改成功");
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getName()!=null,Category::getName,category.getName());
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return Result.success(list);
    }
}
