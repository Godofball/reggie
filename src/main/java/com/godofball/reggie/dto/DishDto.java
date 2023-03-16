package com.godofball.reggie.dto;


import com.godofball.reggie.pojo.Dish;
import com.godofball.reggie.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

    @Override
    public String toString() {
        return "DishDto{" +
                "flavors=" + flavors +
                ", categoryName='" + categoryName + '\'' +
                ", copies=" + copies +
                ", dish=" + super.toString() +
                '}';
    }
}
