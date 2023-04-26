package me.kiritoasuna.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.kiritoasuna.reggie.dto.SetmealDto;
import me.kiritoasuna.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void  saveWithDish(SetmealDto setmealDto);//新增套餐，同时保存和菜品的关联关系
    void  removeWithDish(List<Long> ids);
}
