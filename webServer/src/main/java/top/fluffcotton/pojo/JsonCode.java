package top.fluffcotton.pojo;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: JsonCode
 * @Description: 用于json返回的code
 * @date 2020.07.31 22:22
 */
public enum JsonCode {
    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 没有好友
     */
    NOT_HAVE_FRIEND(300),
    /**
     * 用户名已经存在
     */
    USER_HAVE(301),
    /**
     * 用户未登录
     */
    NOT_LOGIN(302),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(303),
    /**
     * 验证码失效
     */
    VCODE_IS_NULL(304),
    /**
     * 验证码错误
     */
    VCODE_IS_ERROR(305),
    /**
     * 密码错误
     */
    PASS_ERROR(306),
    /**
     * 标识不唯一
     */
    SIGN_NOT_ONLY(307),
    /**
     * 添加失败
     */
    ADD_FAIL(308),
    /**
     * 查询失败
     */
    FIND_FAIL(309),
    /**
     * 修改失败
     */
    CHANGE_FAIL(310),
    /**
     * 被绑定
     */
    BIND(311),
    /**
     * 没有权限
     */
    NOT_POWER(312),
    /**
     * 未查询到
     */
    NOT_HAS(313),
    /**文件为null*/
    FILE_IS_NULL(314),
    /**
     * 异常
     */
    EXCEPTION(400),
    ;

    /**
     * 代码
     */
    private final int code;

    JsonCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
