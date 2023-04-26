package me.kiritoasuna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.kiritoasuna.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
