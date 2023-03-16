package com.godofball.reggie.service;

import com.godofball.reggie.pojo.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;

    @Test
    public void testGetAll(){
        System.out.println(categoryService);
        List<Category> list = categoryService.list();
        list.forEach(System.out::println);
    }
}
