package top.fluffcotton.pojo;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupFile
 * @Description: 存储发送到html的群与文件夹pojo
 * @date 2020.08.23 22:17
 */
public class GroupFolder {

    /**
     * 文件夹和群的id
     */
    private String id;
    /**
     * 群名称/文件夹名称
     */
    private String groupName;
    /**
     * 群号，文件夹无效
     */
    private String groupId;
    /**
     * 是否是文件夹
     */
    private boolean hasChildren;

    public GroupFolder() {
    }

    public GroupFolder(String id, String groupName, String groupId, boolean hasChildren) {
        this.id = id;
        this.groupName = groupName;
        this.groupId = groupId;
        this.hasChildren = hasChildren;
    }
    /**
     * 根据group类直接构建
     * @Title GroupFile
     * @Description 根据group类直接构建
     * @param group 群
     * @return 群文件夹
     * @author 张逸辰
     * @Date 2020/8/23 22:22
     */
    public GroupFolder(QqGroup group){
        this.id = group.getGroupid();
        this.groupName = group.getGroupname();
        this.hasChildren = false;
        this.groupId = group.getGroupid();
    }
    /**
     * 根据文件夹直接构建
     * @Title GroupFile
     * @Description 根据文件夹直接构建
     * @param folder 文件夹
     * @return 群文件夹
     * @author 张逸辰
     * @Date 2020/8/23 22:23
     */
    public GroupFolder(Folder folder){
        this.id = String.valueOf(folder.getFileid());
        this.groupId = "";
        this.hasChildren = true;
        this.groupName = folder.getFilename();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

}
