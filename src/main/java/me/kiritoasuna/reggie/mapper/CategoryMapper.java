package me.kiritoasuna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.kiritoasuna.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
