package me.kiritoasuna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.dto.DishDto;
import me.kiritoasuna.reggie.dto.OrdersDto;
import me.kiritoasuna.reggie.entity.Dish;
import me.kiritoasuna.reggie.entity.OrderDetail;
import me.kiritoasuna.reggie.entity.Orders;
import me.kiritoasuna.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
         log.info("订单数据:"+orders.toString());
         orderService.submit(orders);

        return R.success("下单成功！");
    }

    @GetMapping("/userPage")
    public R<Page>  userpage(int page, int pageSize){
        //构造分页构造器
        Page<Orders> ordesPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<Orders> ordersPage = orderService.page(ordesPage);
        return R.success(ordersPage);

        //对象拷贝
}
    @GetMapping("/page")
    public R<Page>  page(int page,int pageSize){
        Page<OrdersDto> mypage = orderService.mypage(page, pageSize);

        return R.success(mypage);
    }
    }
