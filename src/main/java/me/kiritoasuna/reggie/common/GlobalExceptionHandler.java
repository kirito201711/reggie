package me.kiritoasuna.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.Controller;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler  {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) //处理sql异常
    public R<String>  excptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg =    split[2]+"已存在!";
            return  R.error(msg);
        }
      return R.error("未知错误!");

    }
    @ExceptionHandler(CustomException.class) //处理Custom异常
    public R<String>  excptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());

    }
}
