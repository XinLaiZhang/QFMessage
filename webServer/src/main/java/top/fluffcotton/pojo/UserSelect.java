package top.fluffcotton.pojo;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: UserSelect
 * @Description: 用于查询目录内用户类
 * @date 2020.08.24 18:02
 */
public class UserSelect extends User {
    /**
     * 是否查询为根目录
     */
    private boolean isRoot;

    public UserSelect() {
    }

    public UserSelect(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public UserSelect(User user){
        this.setId(user.getId());
        this.setQq(user.getQq());
        this.setUsername(user.getUsername());
        this.setRobotqq(user.getRobotqq());
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }
}
