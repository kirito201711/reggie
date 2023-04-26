package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.kiritoasuna.reggie.entity.ShoppingCart;
import me.kiritoasuna.reggie.mapper.ShoppingCartMapper;
import me.kiritoasuna.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
