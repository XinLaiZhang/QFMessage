package top.fluffcotton.service;

import top.fluffcotton.pojo.AuthCode;
import top.fluffcotton.pojo.JsonCode;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: SecurityCode
 * @Description: 实现QQ消息验证码
 * @date 2020.08.26 16:05
 */
public interface SecurityCode {


    /**
     * 发送验证码给用户
     *
     *
     * @param authCode 验证码pojo
     * @return 是否发送成功
     * @Title sendSecurityCode
     * @Description 发送验证码给用户
     * @author 张逸辰
     * @Date 2020/8/26 16:23
     */
    JsonCode sendSecurityCode(AuthCode authCode);

    /**
     * 获取验证码用于验证
     *
     * @param sign 唯一标志
     * @return 返回验证码
     * @Title getSecurityCode
     * @Description 获取验证码用于验证
     * @author 张逸辰
     * @Date 2020/8/26 16:24
     */
    String getSecurityCode(Object sign);

}
