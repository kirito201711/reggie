package me.kiritoasuna.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.entity.Category;
import me.kiritoasuna.reggie.entity.Employee;
import me.kiritoasuna.reggie.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String>  save(@RequestBody Category category){
     log.info("category"+category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page>   page(int page, int pageSize){
        log.info("page:"+page+" pagesize:"+pageSize);
        //构造分页查询器
        Page pageinfo = new Page(page,pageSize);
        //构造条件查询器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.orderByDesc(Category::getSort);//排序条件
        //执行查询
        categoryService.page(pageinfo,queryWrapper);
        return  R.success(pageinfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类,id为： "+ids);

        categoryService.remove(ids);
        return R.success("分类信息删除成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
         log.info("修改分类信息 "+category);
        categoryService.updateById(category);
        return  R.success("分类信息修改成功");
    }

    /**
     * 根据条件查询
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>>  list(Category category){


         LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
         if(category.getType()!=null){
             lambdaQueryWrapper.eq(Category::getType,category.getType());
             lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
             final var list = categoryService.list(lambdaQueryWrapper);
             return R.success(list);
         }
         if (category.getName()==null){
             List<Category> categoryList = categoryService.list();
             return  R.success(categoryList);
         }
       return R.error("获取菜品种类失败!");
    }
}
