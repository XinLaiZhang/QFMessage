package top.fluffcotton.pojo;

import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupList
 * @Description: 群名单
 * @date 2020.08.30 16:54
 */
public class GroupList {
    /**
     * QQ号
     */
    private String qq;

    /**
     * 昵称
     */
    private String name;

    /**完成情况*/
    private boolean finish;

    /**
     * 保存属性
     */
    private List<String> saveAttr;

    public GroupList() {
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public List<String> getSaveAttr() {
        return saveAttr;
    }

    public void setSaveAttr(List<String> saveAttr) {
        this.saveAttr = saveAttr;
    }
}
