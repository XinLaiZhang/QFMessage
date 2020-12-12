package top.fluffcotton.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.fluffcotton.pojo.User;
import top.fluffcotton.service.RSATool;
import top.fluffcotton.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author : 张逸辰
 * @ClassName: UserHandlerInterceptor
 * @Description :拦截后台管理避免未登录及进入
 * @date : 2020年8月28日 下午5:39
 */
public class UserHandlerInterceptor implements HandlerInterceptor {
    /**
     * 用户管理类
     */
    private final UserService userService;

    /**
     * 解密工具
     */
    private final RSATool rsaTool;

    public UserHandlerInterceptor(UserService userService, RSATool rsaTool) {
        this.userService = userService;
        this.rsaTool = rsaTool;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        boolean isPass = true;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            isPass = false;
        } else {
            String username = null;
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();
                    break;
                }
            }
            if (username == null) {
                isPass = false;
            } else {
                username = rsaTool.decrypt(username);
                User user = userService.selectUserByUsername(username);
                if (user == null) {
                    isPass = false;
                }
            }
        }
        if (!isPass) {
            StringBuffer url = request.getRequestURL();
            response.sendRedirect(url.substring(0, url.indexOf("user")) + "login.html");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler,
                           ModelAndView modelAndView) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        // TODO 自动生成的方法存根

    }

}
