package me.kiritoasuna.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.dto.DishDto;
import me.kiritoasuna.reggie.entity.Category;
import me.kiritoasuna.reggie.entity.Dish;
import me.kiritoasuna.reggie.entity.DishFlavor;
import me.kiritoasuna.reggie.service.CategoryService;
import me.kiritoasuna.reggie.service.DishFlavorService;
import me.kiritoasuna.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
public class DishController {

     @Autowired
     private DishService dishService;

     @Autowired
     private DishFlavorService dishFlavorService;

     @Autowired
     private CategoryService categoryService;
     /**
      * 新增菜品
      * @param dishDto
      * @return
      */
     @PostMapping
     public R<String> save(@RequestBody DishDto dishDto){
          System.out.println(dishDto.toString());
          dishService.saveWithFlavor(dishDto);
          return R.success("新增菜品成功！");
     }

     /**
      * 菜品信息分页查询
      * @param page
      * @param pageSize
      * @param name
      * @return
      */
     @GetMapping("/page")
     public R<Page>  page(int page,int pageSize,String name){
          //构造分页构造器
          Page<Dish> dishPage = new Page<>(page,pageSize);
          Page<DishDto> dishDtoPage = new Page<>();

          LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
          lambdaQueryWrapper.like(name!=null,Dish::getName,name);
          lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
          dishService.page(dishPage, lambdaQueryWrapper);
          //对象拷贝
          BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
          final var records = dishPage.getRecords();
          List<DishDto> list =  records.stream().map((item)->{
             DishDto dishDto = new DishDto();

             BeanUtils.copyProperties(item,dishDto);
             final var categoryId = item.getCategoryId();
             Category category = categoryService.getById(categoryId);
             final var name0 = category.getName();
             dishDto.setCategoryName(name0);
             return dishDto;
          }).collect(Collectors.toList());

         dishDtoPage.setRecords(list);
         return R.success(dishDtoPage);
     }

     @GetMapping("/{id}")
     public R<DishDto> getone(@PathVariable  Long id){
         final var dishDto = dishService.getByIdWithFlavor(id);

         return  R.success(dishDto);
     }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        System.out.println(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功！");
    }

    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//         LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//         lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//         lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
//         lambdaQueryWrapper.eq(Dish::getStatus,1);
//        final var list = dishService.list(lambdaQueryWrapper);
//
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        lambdaQueryWrapper.eq(Dish::getStatus,1);


        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
     List<DishDto> dishDtoList  = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId(); //当前菜品Id
             LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
             queryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorsList = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorsList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
