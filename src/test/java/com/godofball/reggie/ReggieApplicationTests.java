package com.godofball.reggie;

import com.godofball.reggie.mapper.EmployeeMapper;
import com.godofball.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    EmployeeService employeeService;
    @Test
    void testEmployeeMapper(){
        System.out.println(employeeMapper);
        System.out.println(employeeService);
    }



}
