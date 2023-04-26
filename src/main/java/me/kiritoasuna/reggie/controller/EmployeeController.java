package me.kiritoasuna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.entity.Employee;
import me.kiritoasuna.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp==null){
            return R.error("登录失败，用户名或密码错误！");
        }
        if(!emp.getPassword().equals(password)){

            return R.error("登录失败，用户名或密码错误！");
        }
        if(emp.getStatus()==0){
            return  R.error("账号已禁用");
        }
        httpServletRequest.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
         log.info("员工信息："+employee.toString());

         employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//         employee.setCreateTime(LocalDateTime.now());
//         employee.setUpdateTime(LocalDateTime.now());
          Long empID = (Long) request.getSession().getAttribute("employee");
//         employee.setCreateUser(empID);
//         employee.setUpdateUser(empID);
        boolean save = employeeService.save(employee);
        return R.success("新增员工成功！");
    }
         @GetMapping("/page")
          public R<Page>   page(int page,int pageSize,String name){
          log.info("page:"+page+" pagesize:"+pageSize+" name:"+name);
         //构造分页查询器
          Page pageinfo = new Page(page,pageSize);
         //构造条件查询器
          LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
          queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
          queryWrapper.orderByDesc(Employee::getUpdateTime);//排序条件
         //执行查询
          employeeService.page(pageinfo,queryWrapper);
          return  R.success(pageinfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long id = Thread.currentThread().getId();
        log.info("当前线程id为:"+id);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息更改成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> getbyid(@PathVariable String id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
