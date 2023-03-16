package com.godofball.reggie.service;

import com.godofball.reggie.pojo.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;

    @Test
    void save(){
        Employee employee = new Employee();
        employee.setName("xiang");
        employee.setUsername("xiang");
        employee.setCreateTime(new Date());
        employee.setCreateUser(342353L);
        employee.setUpdateUser(342353L);
        employee.setPassword("353drdg");
        employee.setSex("0");
        employee.setPhone("34w45w");
        employee.setIdNumber("453636363");
        employee.setUpdateTime(new Date());
        System.out.println(employeeService.save(employee));
    }
}
