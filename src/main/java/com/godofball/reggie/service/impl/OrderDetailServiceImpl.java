package com.godofball.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godofball.reggie.mapper.OrderDetailMapper;
import com.godofball.reggie.pojo.OrderDetail;
import com.godofball.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
