package com.godofball.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godofball.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {

    /**
     * 根据id删除分类，如果该分类有关联菜品或套餐，删除失败
     *
     * @param id
     */
    void remove(Long id);
}
