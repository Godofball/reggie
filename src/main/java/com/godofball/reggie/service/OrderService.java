package com.godofball.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godofball.reggie.pojo.Orders;

public interface OrderService extends IService<Orders> {

    void submit(Orders orders);

}
