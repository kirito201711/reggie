package me.kiritoasuna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.kiritoasuna.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
