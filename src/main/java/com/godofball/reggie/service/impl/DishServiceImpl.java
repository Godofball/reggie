package com.godofball.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godofball.reggie.dto.DishDto;
import com.godofball.reggie.exception.DishException;
import com.godofball.reggie.mapper.DishMapper;
import com.godofball.reggie.pojo.Category;
import com.godofball.reggie.pojo.Dish;
import com.godofball.reggie.pojo.DishFlavor;
import com.godofball.reggie.service.CategoryService;
import com.godofball.reggie.service.DishFlavorService;
import com.godofball.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
//    @Autowired
//    private CategoryService categoryService;

    @Value("${reggie.path}")
    private String basePath;

    @Override
    @Transactional
    public void saveDishAndFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();//获取生成的菜品ID
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor -> flavor.setDishId(dishId));//为每个口味赋菜品ID
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getDishDtoById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateDishAndFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        Long dishId = dishDto.getId();//将菜品关联的口味信息删除
        LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(flavorLambdaQueryWrapper);
        //添加新的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor -> flavor.setDishId(dishId));
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void deleteDishAndFlavorWithBatch(List<Long> ids) {
        List<Dish> dishList = this.listByIds(ids);

        for (Dish dish : dishList) {
            if (dish.getStatus()==1){
                throw new DishException("启售中的菜品不能删除");
            }
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
            dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        }
        this.removeByIds(ids);

        //删除菜品对应的图片
        for (Dish dish : dishList) {
            String imagePath = basePath+dish.getImage();
            File file = new File(imagePath);
            log.info(imagePath+file.exists());
            file.delete();
            log.info(imagePath+file.exists()+"");
        }

    }

    @Override
    @Transactional
    public void updateStatusByBatch(Integer status, List<Long> ids) {
        if (status>1&&status<0){
            throw new DishException("修改失败");
        }
        List<Dish> dishes = new LinkedList<>();
        ids.forEach(id->{
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishes.add(dish);
        });
        this.updateBatchById(dishes);
    }
}
