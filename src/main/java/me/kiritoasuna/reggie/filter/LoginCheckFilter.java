package me.kiritoasuna.reggie.filter;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.kiritoasuna.reggie.common.BaseContext;
import me.kiritoasuna.reggie.common.R;
import me.kiritoasuna.reggie.entity.Employee;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
     //路径匹配器
     public static  final AntPathMatcher PATH_MATCHER= new AntPathMatcher();//专门进行路径比较

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        log.info("拦截到请求"+request.getRequestURI());
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        Boolean check = check(urls, requestURI);
        if (check==true){
            log.info("不用处理,放行"+requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        if( request.getSession().getAttribute("employee")!=null){   //员工登录判断
            log.info("用户已经登录，id为"+request.getSession().getAttribute("employee"));

            Long empid = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empid);
            Long id = Thread.currentThread().getId();

            log.info("当前线程id为:"+id);
            filterChain.doFilter(request,response);
            return;

            }
        if( request.getSession().getAttribute("user")!=null){   //移动端用户登录判断
            log.info("用户已经登录，id为"+request.getSession().getAttribute("user"));

            Long userid = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userid);
            Long id = Thread.currentThread().getId();

            log.info("当前线程id为:"+id);
            filterChain.doFilter(request,response);
            return;

        }

            log.info("用户未登录！");

            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要匹配
     * @param requestURI
     * @return
     */
    public Boolean check(String[] urls,  String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
         return false;
    }
}
