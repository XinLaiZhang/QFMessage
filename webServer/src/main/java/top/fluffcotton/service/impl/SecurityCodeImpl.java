package top.fluffcotton.service.impl;

import net.mamoe.mirai.contact.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fluffcotton.pojo.AuthCode;
import top.fluffcotton.pojo.JsonCode;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.SecurityCode;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.DelayQueue;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: SecurityCodeImpl
 * @Description: 验证码处理
 * @date 2020.08.26 16:25
 */
@Service
public class SecurityCodeImpl implements SecurityCode {


    /**
     * 前为唯一标志，后为验证码
     */
    private final Map<Object, AuthCode> authCodeList = new HashMap<>(5);
    /**
     * 验证码生命周期
     */
    private final Queue<AuthCode> authCodeEndTime = new DelayQueue<>();
    /**
     * 发送消息
     */
    @Autowired
    private BotService botService;

    /**
     * 发送验证码给用户
     *
     * @param authCode 验证码pojo
     * @return 状态码
     * @Title sendSecurityCode
     * @Description 发送验证码给用户
     * @author 张逸辰
     * @Date 2020/8/26 16:23
     */
    @Override
    public JsonCode sendSecurityCode(AuthCode authCode) {
        //清空过期验证码
        clearAuthCode();
        //获取好友列表
        Friend friend = botService.getFriend(Long.parseLong(authCode.getQq()));
        //获取标识是否存在
        AuthCode code = authCodeList.get(authCode.getSign());
        if(code != null) {
            //发送QQ相同，则认为为同一任务
            if (code.getQq().equals(authCode.getQq())) {
                authCode.setCode(code.getCode());
            } else {
                //任务不同，则认为标识重复
                return JsonCode.SIGN_NOT_ONLY;
            }
        }
        //判断是否添加了好友
        if (friend != null) {
            String vCode = authCode.getCode();
            //没有有效vCode 重新生成
            if (vCode == null) {
                vCode = String.valueOf(new Random().nextInt(999999 - 100000) + 100000);
                authCode.setCode(vCode);
                //添加到有效vCode列表
                authCodeList.put(authCode.getSign(), authCode);
                //添加失效时间
                authCodeEndTime.add(authCode);
            }
            LocalTime localTime = LocalTime.ofNanoOfDay(authCode.getTime()*1000000L);
            //发送验证码
            friend.sendMessage("您正在" + authCode.getTip() + "，验证码：" + vCode + "，该验证码仅用于验证身份，" + localTime.getMinute() + "分钟内有效");
        }else
        {
            return JsonCode.NOT_HAVE_FRIEND;
        }
        return JsonCode.SUCCESS;
    }

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
    @Override
    public String getSecurityCode(Object sign) {
        clearAuthCode();
        AuthCode authCode = authCodeList.get(sign);
        if(authCode == null){
            return null;
        }
        return authCode.getCode();
    }

    /**
     * 清除过期验证码
     * @Title clearAuthCode
     * @Description 清除过期验证码
     * @author 张逸辰
     * @Date 2020/8/26 16:34
     */
    private void clearAuthCode() {
        //清除过期vCode
        AuthCode authCode;
        while ((authCode = authCodeEndTime.poll()) != null) {
            if (authCodeList.get(authCode) != null) {
                authCodeList.remove(authCode.getSign());
            }
        }
    }
}
