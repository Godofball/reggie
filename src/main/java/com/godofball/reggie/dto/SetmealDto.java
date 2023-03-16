package com.godofball.reggie.dto;


import com.godofball.reggie.pojo.Setmeal;
import com.godofball.reggie.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
