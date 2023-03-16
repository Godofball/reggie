package com.godofball.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.godofball.reggie.common.Result;
import com.godofball.reggie.pojo.Employee;
import com.godofball.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpSession session) {
        //将密码进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据用户名查询数据库表中信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(new QueryWrapper<Employee>().eq("username", employee.getUsername()));

        //判断是否用户是否存在
        if (emp == null) {
            return Result.error("用户不存在");
        }
        //判断是否密码是否错误
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误");
        }
        //判断是否用户状态
        if (emp.getStatus() == 0) {
            return Result.error("账号已禁用");
        }
        //6、登录成功，将用户id存入Session并返回成功结果
        session.setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工信息
     *
     * @param employee
     * @param session
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Employee employee, HttpSession session) {
        log.info("save员工：{}", employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Date date = new Date();
        employee.setCreateTime(date);
        employee.setUpdateTime(date);
        Long empId = (Long) session.getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return Result.success(null);
    }

    @GetMapping("/page")
    public Result<Page> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "name", required = false) String name) {
        log.info("分页数据——page:{},pageSize:{},name:{}", page, pageSize, name);
        //条件过滤器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(name), Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);

        Page<Employee> employeePage = employeeService.page(new Page<>(page, pageSize), wrapper);
        return Result.success(employeePage);
    }



    @GetMapping("/{id}")
    public Result<Employee> getEmpById(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);
        if (employee==null){
            return Result.error("未找到该员工");
        }
        return Result.success(employee);
    }

    @PutMapping
    public Result<String> update(@RequestBody Employee employee, HttpSession session) {
        log.info("update员工：{}", employee);
        Date date = new Date();
//        employee.setUpdateTime(date);
        Long empId = (Long) session.getAttribute("employee");
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return Result.success("修改成功");
    }


}
