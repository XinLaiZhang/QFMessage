package top.fluffcotton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fluffcotton.pojo.AuthCode;
import top.fluffcotton.pojo.JsonCode;
import top.fluffcotton.pojo.User;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.RSATool;
import top.fluffcotton.service.SecurityCode;
import top.fluffcotton.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: Register
 * @Description: 注册api
 * @date 2020.07.31 21:50
 */
@RestController
@RequestMapping("api/")
public class Register {

    /**
     * 机器人管理器
     */
    @Autowired
    private BotService botManager;

    /**
     * 用户操作类
     */
    @Autowired
    private UserService userService;

    /**
     * rsa加密工具
     */
    @Autowired
    private RSATool rsaTool;

    /**
     * 验证码管理类
     */
    @Autowired
    private SecurityCode securityCode;


    /**
     * 获取机器人qq号
     *
     * @return {
     * code:200,
     * qq:机器人qq号
     * }
     * @Title getRobot
     * @Description 获取机器人qq号
     * @author 张逸辰
     * @Date 2020/7/31 22:26
     */
    @RequestMapping("/getRobot")
    public Map<String, Object> getRobot() {
        Map<String, Object> map = new HashMap<>(5);
        map.put("code", JsonCode.SUCCESS.toString());
        map.put("qq", botManager.defaultBot().getId());
        return map;
    }

    /**
     * 获取验证码
     *
     * @param qq 好友
     * @return {
     * code:200 //成功
     * code:300 //没有好友
     * }
     * @Title getBindCode
     * @Description 获取验证码
     * @author 张逸辰
     * @Date 2020/7/31 23:08
     */
    @RequestMapping("/getBindCode")
    public Map<String, Object> getBindCode(String qq) {
        Map<String, Object> map = new HashMap<>(5);
        AuthCode authCode = new AuthCode(qq, "注册【清风】群管家", 5, "BindCode" + qq);
        JsonCode jsonCode = securityCode.sendSecurityCode(authCode);
        switch (jsonCode) {
            case SUCCESS:
                map.put("code", JsonCode.SUCCESS.toString());
                break;
            case NOT_HAVE_FRIEND:
                map.put("code", JsonCode.NOT_HAVE_FRIEND.toString());
                break;
            default:
                map.put("code", JsonCode.EXCEPTION.toString());
        }
        return map;
    }

    /**
     * 根据qq，密码，验证码直接注册
     *
     * @param qq       管理者qq
     * @param password 密码
     * @param vCode    验证码
     * @param response 回复
     * @return {
     * code:200注册成功
     * code:400出现异常
     * code:301用户已存在
     * code:305验证码错误
     * }
     * @Title register
     * @Description 根据qq，密码，验证码直接注册
     * @author 张逸辰
     * @Date 2020/8/20 22:48
     */
    @RequestMapping("/register")
    public Map<String, Object> register(String qq, String password, String vCode, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(5);
        //查询验证码是否正确
        String securityCode = this.securityCode.getSecurityCode("BindCode" + qq);
        //判定验证码是否存在，并且是否与填写相同
        if (securityCode != null && securityCode.equals(vCode)) {
            //查询qq是否存在
            User selectUser = userService.selectUserByQq(qq);
            if (selectUser == null) {
                User user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setUsername(qq);
                user.setPassword(password);
                user.setQq(qq);
                user.setRobotqq(String.valueOf(botManager.defaultBot().getId()));
                userService.insertUser(user);
                map.put("code", JsonCode.SUCCESS.toString());
                try {
                    Cookie cookie = new Cookie("username", rsaTool.encrypt(user.getUsername()));
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } catch (Exception e) {
                    map.put("code", JsonCode.EXCEPTION.toString());
                    e.printStackTrace();
                }
            } else {
                map.put("code", JsonCode.USER_HAVE.toString());
            }
        } else {
            //验证码错误
            map.put("code", JsonCode.VCODE_IS_ERROR.toString());
        }
        return map;
    }

    /**
     * 正常注册api
     * 密码用md5加密后使用RSA加密
     *
     * @param username 用户名
     * @param password 密码
     * @return {
     * code:200成功
     * code:400异常
     * code:301用户存在
     * }
     * @Title register
     * @Description 正常注册api
     * @author 张逸辰
     * @Date 2020/8/1 21:19
     */
    @RequestMapping("/register_old")
    public Map<String, Object> register(String username, String password, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(5);
        //查询用户名是否存在
        User selectUser = userService.selectUserByUsername(username);
        if (selectUser == null) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(username);
            user.setPassword(password);
            userService.insertUser(user);
            map.put("code", JsonCode.SUCCESS.toString());
            try {
                Cookie cookie = new Cookie("username", rsaTool.encrypt(username));
                response.addCookie(cookie);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", JsonCode.EXCEPTION.toString());
            }
        } else {
            map.put("code", JsonCode.USER_HAVE.toString());
        }
        return map;
    }


    /**
     * 绑定qq
     *
     * @param vCode 验证码
     * @param qq    管理者qq
     * @return {
     * code:200成功
     * code:400异常
     * code:302未登录
     * code:303用户未存在
     * code:304验证码过期
     * code:305验证码错误
     * }
     * @Title bindQQ
     * @Description 绑定qq
     * @author 张逸辰
     * @Date 2020/8/1 22:20
     */
    @RequestMapping("/bindQQ")
    public Map<String, Object> bindQQ(String qq, String vCode, HttpServletRequest request, @CookieValue("username") String username) {
        Map<String, Object> map = new HashMap<>(5);
        //查询验证码是否正确
        String securityCode = this.securityCode.getSecurityCode("BindCode" + qq);
        //获取code对应qq
        if (securityCode != null) {
            try {
                username = rsaTool.decrypt(username);
                //未登录
                if (username == null) {
                    map.put("code", JsonCode.NOT_LOGIN.toString());
                } else {
                    //查询账号是否存在
                    User user = userService.selectUserByUsername(username);
                    if (user == null) {
                        map.put("code", JsonCode.USER_NOT_EXIST.toString());
                    } else if (securityCode.equals(vCode)) {
                        //更新user信息
                        user.setQq(qq);
                        userService.updateUser(user);
                        map.put("code", JsonCode.SUCCESS.toString());
                    } else {
                        //验证码错误
                        map.put("code", JsonCode.VCODE_IS_ERROR.toString());
                    }
                }
            } catch (Exception e) {
                map.put("code", JsonCode.EXCEPTION.toString());
                e.printStackTrace();
            }
        } else {
            //验证码过期
            map.put("code", JsonCode.VCODE_IS_NULL.toString());
        }
        return map;
    }

    /**
     * 根据用户名密码登陆
     *
     * @param username 用户名
     * @param password 密码
     * @param remember 记住我
     * @param response 回复
     * @return {
     * code:200成功
     * code:303用户不存在
     * code:400异常
     * code:306密码错误
     * }
     * @Title login
     * @Description 根据用户名密码登陆
     * @author 张逸辰
     * @Date 2020/8/21 21:45
     */
    @RequestMapping("/login")
    public Map<String, Object> login(String username, String password, boolean remember, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(5);
        User user = userService.selectUserByUsernameOrQq(username);
        if (user == null) {
            //用户不存在
            map.put("code", JsonCode.USER_NOT_EXIST.toString());
        } else {
            try {
                if (rsaTool.decrypt(user.getPassword()).equals(rsaTool.decrypt(password))) {
                    //登陆成功
                    map.put("code", JsonCode.SUCCESS.toString());
                    Cookie cookie = new Cookie("username", rsaTool.encrypt(user.getUsername()));
                    if (remember) {
                        //记住用户7天
                        cookie.setMaxAge(7 * 24 * 60 * 60);
                    }
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } else {
                    //密码错误
                    map.put("code", JsonCode.PASS_ERROR.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", JsonCode.EXCEPTION.toString());
            }
        }
        return map;
    }

    /**
     * 退出登陆
     *
     * @param request  请求
     * @param response 响应
     * @return {
     * code:200成功
     * code:302用户未登录
     * }
     * @Title loginOut
     * @Description 退出登陆
     * @author 张逸辰
     * @Date 2020/8/21 21:59
     */
    @RequestMapping("/loginOut")
    public Map<String, Object> loginOut(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(5);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("username".equals(cookie.getName())) {
                map.put("code", JsonCode.SUCCESS.toString());
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }
        map.computeIfAbsent("code", k -> JsonCode.NOT_LOGIN.toString());
        return map;
    }

    /**
     * 获取管理员qq
     *
     * @param request 请求
     * @return {
     * code:200成功
     * data:管理员qq
     * code:303 用户不存在
     * code:400异常
     * code:302用户未登录
     * }
     * @Title getAdminQQ
     * @Description 获取管理员qq
     * @author 张逸辰
     * @Date 2020/8/21 22:16
     */
    @RequestMapping("/getAdminQQ")
    public Map<String, Object> getAdminQQ(HttpServletRequest request, @CookieValue("username") String username) {
        Map<String, Object> map = new HashMap<>(5);
        Cookie[] cookies = request.getCookies();
        try {
            username = rsaTool.decrypt(username);
            User user = userService.selectUserByUsername(username);
            if (user == null) {
                map.put("code", JsonCode.USER_NOT_EXIST.toString());
            } else {
                map.put("code", JsonCode.SUCCESS.toString());
                map.put("data", user.getQq());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", JsonCode.EXCEPTION.toString());
        }
        map.computeIfAbsent("code", k -> JsonCode.NOT_LOGIN.toString());
        return map;
    }

}
