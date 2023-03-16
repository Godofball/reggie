package com.godofball.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godofball.reggie.dto.SetmealDto;
import com.godofball.reggie.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveSetmealDto(SetmealDto setmealDto);

    void removeSetmealDtoBatch(List<Long> ids);

    SetmealDto getSetmealDtoById(Long id);

    void updateSetmealDto(SetmealDto setmealDto);

}
