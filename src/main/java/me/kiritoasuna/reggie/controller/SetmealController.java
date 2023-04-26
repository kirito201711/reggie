package me.kiritoasuna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.dto.SetmealDto;
import me.kiritoasuna.reggie.entity.Category;
import me.kiritoasuna.reggie.entity.Dish;
import me.kiritoasuna.reggie.entity.Setmeal;
import me.kiritoasuna.reggie.service.CategoryService;
import me.kiritoasuna.reggie.service.DishService;
import me.kiritoasuna.reggie.service.SetmealDishService;
import me.kiritoasuna.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    DishService dishService;
    @Autowired
    private SetmealDishService setmealDishServicel;


    @Autowired
  private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /**
     * 套餐分页查寻
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page>  list(int page,int pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        Page<SetmealDto> setmealDtoPage = new Page<>();


        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!= null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPage,lambdaQueryWrapper);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

         var records = setmealPage.getRecords();
    List<SetmealDto>  list = records.stream().map((item->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            final var categoryId = item.getCategoryId();
            final var category = categoryService.getById(categoryId);
            if(category!=null){
                final var categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return  setmealDto;
        })).collect(Collectors.toList());
       setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }


    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
       queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
       queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

//    @GetMapping("/dish/{dishId}")
//    public R<Dish> dishinfo(@PathVariable Long dishId){
//        Dish dish = dishService.getById(dishId);
//        if (dish==null){
//            return R.error("查询失败！");
//        }
//        return R.success(dish);
//    }
    @DeleteMapping
    public  R<String> delect(@RequestParam List<Long> ids){

      setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功！");
    }

}
