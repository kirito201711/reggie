package me.kiritoasuna.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.kiritoasuna.reggie.entity.Category;

public interface CategoryService extends IService<Category> {


    public void remove(Long id);
}
