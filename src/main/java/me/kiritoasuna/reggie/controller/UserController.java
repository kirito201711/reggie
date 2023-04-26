package me.kiritoasuna.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mysql.cj.protocol.x.Notice;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.entity.User;
import me.kiritoasuna.reggie.service.UserService;
import me.kiritoasuna.reggie.untils.SendSms;
import me.kiritoasuna.reggie.untils.ValidateCodeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody  User user, HttpSession session) throws Exception {

        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码："+code);
//            SendSms.sendMessages(String.valueOf(code));
            session.setAttribute(phone,code);
            log.info("sendSMS succeeful");
       return R.success("验证码发送成功！");

        }

        return R.error("验证码发送失败！");
    }

    /**
     * 移动端用户登录
     * @return
     */
    @PostMapping("/login")
    public  R<User> login(@RequestBody Map map,HttpSession session){

        String phone = map.get("phone").toString();
        log.info(phone);
        String code = map.get("code").toString();
        log.info(code);
        String codeInSession = (String) session.getAttribute(phone);
        System.out.println(codeInSession);

        if (codeInSession !=null && codeInSession.equals(code)){

            LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user ==null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
             session.setAttribute("user",user.getId());
             return  R.success(user);


        }
        return R.error("登录失败");
    }

}
