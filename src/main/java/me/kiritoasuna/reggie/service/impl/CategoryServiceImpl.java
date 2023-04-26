package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import me.kiritoasuna.reggie.common.CustomException;
import me.kiritoasuna.reggie.entity.Category;
import me.kiritoasuna.reggie.entity.Dish;
import me.kiritoasuna.reggie.entity.Setmeal;
import me.kiritoasuna.reggie.mapper.CategoryMapper;
import me.kiritoasuna.reggie.service.CategoryService;
import me.kiritoasuna.reggie.service.DishService;
import me.kiritoasuna.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     *
     * 根据id删除分类,删除之前进行判断，判断当前菜品是否关联分类或套餐
     * @param id
     */
    @Override
    public void remove(Long id) {
         //查询当前分类是否关联了菜品，如果已经关联，则抛出一个业务异常
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        final var count1 = dishService.count(lambdaQueryWrapper);

        if (count1>0){
             //抛出业务异常
            throw new CustomException("当前分类关联了菜品，不能删除");
         }
         //查询当前分类是否关联了套餐,如果已经关联，则抛出一个业务异常
         LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
         lambdaQueryWrapper1.eq(Setmeal::getCategoryId,id);
        final var count2= setmealService.count(lambdaQueryWrapper1);
        if(count2>0){
             //抛出业务异常
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);

    }
}
