package top.fluffcotton.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: UserMap
 * @Description: 用户列表类，保存数据
 * @date 2020.08.07 22:16
 */
public class UserMap {
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * qq号
     */
    private String qq;
    /**
     * 保存消息
     */
    private List<String> saveMsg = new ArrayList<>();

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public List<String> getSaveMsg() {
        return saveMsg;
    }

    public void setSaveMsg(List<String> saveMsg) {
        this.saveMsg = saveMsg;
    }
}
