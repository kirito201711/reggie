package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.kiritoasuna.reggie.entity.SetmealDish;
import me.kiritoasuna.reggie.mapper.SetmealDishMapper;
import me.kiritoasuna.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
