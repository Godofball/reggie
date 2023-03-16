package com.godofball.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godofball.reggie.dto.DishDto;
import com.godofball.reggie.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveDishAndFlavor(DishDto dishDto);

    DishDto getDishDtoById(Long id);

    void updateDishAndFlavor(DishDto dishDto);

    void deleteDishAndFlavorWithBatch(List<Long> ids);

    void updateStatusByBatch(Integer status,List<Long> ids);
}
