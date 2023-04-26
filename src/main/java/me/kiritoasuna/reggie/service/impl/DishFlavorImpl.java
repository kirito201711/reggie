package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.kiritoasuna.reggie.entity.DishFlavor;
import me.kiritoasuna.reggie.mapper.DishFlavorMapper;
import me.kiritoasuna.reggie.service.DishFlavorService;
import me.kiritoasuna.reggie.service.DishService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
