package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.kiritoasuna.reggie.common.CustomException;
import me.kiritoasuna.reggie.dto.SetmealDto;
import me.kiritoasuna.reggie.entity.Setmeal;
import me.kiritoasuna.reggie.entity.SetmealDish;
import me.kiritoasuna.reggie.mapper.SetmealMapper;
import me.kiritoasuna.reggie.service.SetmealDishService;
import me.kiritoasuna.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto)//新增套餐，同时保存和菜品的关联关系
    {
         this.save(setmealDto);
         var setmealDishes = setmealDto.getSetmealDishes();
         setmealDishes  =   setmealDishes.stream().map((item->{
            item.setSetmealId(setmealDto.getId());
            return item;
        })).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }


    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        final var count = this.count(queryWrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中，不能删除！");

        }
       this.removeByIds(ids);


        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }


}
