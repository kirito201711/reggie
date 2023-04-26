package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.dto.DishDto;
import me.kiritoasuna.reggie.entity.Dish;
import me.kiritoasuna.reggie.entity.DishFlavor;
import me.kiritoasuna.reggie.mapper.DishMapper;
import me.kiritoasuna.reggie.service.DishFlavorService;
import me.kiritoasuna.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     * 新增菜品，同时保存对应口味数据
     * @param dishDto
     */
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        final var id = dishDto.getId();
        var flavors = dishDto.getFlavors();
       flavors =  flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        final var dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        final var flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);
        var flavors = dishDto.getFlavors();
        flavors = dishDto.getFlavors();
        flavors =  flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
