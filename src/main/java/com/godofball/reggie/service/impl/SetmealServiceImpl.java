package com.godofball.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godofball.reggie.dto.SetmealDto;
import com.godofball.reggie.exception.SetmealException;
import com.godofball.reggie.mapper.SetmealMapper;
import com.godofball.reggie.pojo.Setmeal;
import com.godofball.reggie.pojo.SetmealDish;
import com.godofball.reggie.service.SetmealDishService;
import com.godofball.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Value("${reggie.path}")
    private String basePath;

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveSetmealDto(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeSetmealDtoBatch(List<Long> ids) {
        List<Setmeal> setmeals = this.listByIds(ids);

        setmeals.forEach(setmeal -> {
            if(setmeal.getStatus()==1){
                throw new SetmealException("启售中的套餐不能删除");
            }
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
            setmealDishService.remove(setmealDishLambdaQueryWrapper);
        });

        this.removeByIds(ids);

        setmeals.forEach(setmeal -> {
            File file = new File(basePath + setmeal.getImage());
            file.delete();
        });

    }

    @Override
    public SetmealDto getSetmealDtoById(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        setmealDishLambdaQueryWrapper.orderByDesc(SetmealDish::getUpdateTime);
        List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLambdaQueryWrapper);

        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;

    }

    @Override
    @Transactional
    public void updateSetmealDto(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishService.saveBatch(setmealDishes);

    }
}
