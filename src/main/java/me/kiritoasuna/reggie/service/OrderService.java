package me.kiritoasuna.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.kiritoasuna.reggie.dto.OrdersDto;
import me.kiritoasuna.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);
    public Page<OrdersDto> mypage(int page, int pageSize);
}
