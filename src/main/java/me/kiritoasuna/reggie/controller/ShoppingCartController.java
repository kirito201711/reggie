package me.kiritoasuna.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.common.BaseContext;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.entity.ShoppingCart;
import me.kiritoasuna.reggie.service.DishService;
import me.kiritoasuna.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private DishService dishService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

             log.info("购物车数据:"+shoppingCart);
              //设置用户Id,指定当前是哪个用户的购物车数据
             Long currentId = BaseContext.getCurrentId();
             shoppingCart.setUserId(currentId);
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
        //查询当前菜品是否在购物车内
        if (shoppingCart.getDishId()!= null){
            //添加到购物车的是菜品
           lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());

        }else {
            //添加到购物车的是套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart cart = shoppingCartService.getOne(lambdaQueryWrapper);

        //如果已经存在，number+1
             if (cart!=null){
                 cart.setNumber(cart.getNumber()+1);
                 shoppingCartService.updateById(cart);
             }
             else {
                 shoppingCart.setNumber(1);
                 shoppingCart.setCreateTime(LocalDateTime.now());
                 shoppingCartService.save(shoppingCart);
                 cart = shoppingCart;
             }
             // 不存在，添加进数据库

              return R.success(cart);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车......");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,id);
        shoppingCartService.remove(lambdaQueryWrapper);
        return  R.success("购物车已清空!");
    }
}
