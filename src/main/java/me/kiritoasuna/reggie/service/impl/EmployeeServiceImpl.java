package me.kiritoasuna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.kiritoasuna.reggie.entity.Employee;
import me.kiritoasuna.reggie.mapper.EmployeeMapper;
import me.kiritoasuna.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
