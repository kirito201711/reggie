package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.kiritoasuna.reggie.entity.OrderDetail;
import me.kiritoasuna.reggie.mapper.OrderDetailMapper;
import me.kiritoasuna.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
