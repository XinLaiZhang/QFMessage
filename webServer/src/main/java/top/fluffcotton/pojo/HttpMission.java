package top.fluffcotton.pojo;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: HttpMission
 * @Description: 传送到http 的任务pojo
 * @date 2020.09.14 18:42
 */
public class HttpMission {
    /**
     * 任务id						all
     */
    private String M_ID;
    /**
     * 任务名称						all
     */
    private String name;
    /**
     * 任务接受消息是否为私聊			1,接收私聊;2,提醒为私聊
     */
    private Boolean isPrivate;
    /**
     * 开始时间						all
     */
    private Long startTime;
    /**
     * 结束时间，重复任务结束时间		all
     */
    private Long endTime;
    /**
     * 结束消息						1
     */
    private String endMsg;
    /**
     * 提醒消息						all
     */
    private String Msg;
    /**
     * 正则表达式						1
     */
    private String Regex;
    /**
     * 回复消息						1
     */
    private String Reply;
    /**
     * 提醒消息						1
     */
    private String remindMsg;
    /**
     * 提醒是否为私聊					1
     */
    private Boolean remindIsPrivate;
    /**
     * 提醒时间						1
     */
    private Long remindTime;
    /**
     * 保存消息						1
     */
    private String[] saveMsg;
    /**
     * 群号							all
     */
    private String groupID;
    /**
     * 任务状态						all
     */
    private Byte status;
    /**
     * 名单							all
     */
    private String groupList;
    /**
     * 提醒任务是否重复				2
     */
    private Boolean isRepeat;
    /**
     * 重复时间						2
     */
    private Long repeatTime;
    /**
     * 任务类型
     */
    private Integer type;

    public HttpMission() {
    }

    public HttpMission(Mission mission) {
        this.M_ID = mission.getmId();
        this.name = mission.getName();
        this.isPrivate = mission.getIsprivate();
        this.startTime = mission.getStarttime();
        this.endTime = mission.getEndtime();
        this.endMsg = mission.getEndmsg();
        this.Msg = mission.getMsg();
        this.Regex = mission.getRegex();
        this.Reply = mission.getReply();
        this.remindMsg = mission.getRemindmsg();
        this.remindIsPrivate = mission.getIsprivate();
        this.remindTime = mission.getRemindtime();
        this.saveMsg = mission.getSavemsg().split(",");
        this.groupID = mission.getGroupid();
        this.status = mission.getStatus();
        this.groupList = mission.getGrouplist();
        this.type = 2;
    }

    public HttpMission(Remindmission remindmission) {
        this.M_ID = remindmission.getmId();
        this.name = remindmission.getName();
        this.isPrivate = remindmission.getIsprivate();
        this.startTime = remindmission.getStarttime();
        this.endTime = remindmission.getEndtime();
        this.Msg = remindmission.getMsg();
        this.groupID = remindmission.getGroupid();
        this.status = remindmission.getStatus();
        this.groupList = remindmission.getGrouplist();
        this.isRepeat = remindmission.getIsrepeat();
        this.repeatTime = remindmission.getRepeattime();
        this.type = 1;
    }
    /**
     * 获取统计任务
     * @Title getMission
     * @Description 获取统计任务
     * @return 如果type不为2 则为null
     * @author 张逸辰
     * @Date 2020/9/14 20:28
     */
    public Mission getMission(){
        Mission mission = null;
        if(this.type == 2){
            mission = new Mission();
            mission.setmId(this.M_ID);
            mission.setName(this.name);
            mission.setIsprivate(this.isPrivate);
            mission.setStarttime(this.startTime);
            mission.setEndtime(this.endTime);
            mission.setEndmsg(this.endMsg);
            mission.setMsg(this.Msg);
            mission.setRegex(this.Regex);
            mission.setReply(this.Reply);
            mission.setRemindmsg(this.remindMsg);
            mission.setRemindisprivate(this.remindIsPrivate);
            mission.setRemindtime(this.remindTime);
            StringBuffer saveMsg = new StringBuffer();
            for(int i = 0;i < this.saveMsg.length;i++){
                if(i == 0){
                    saveMsg.append(this.saveMsg[i]);
                }else
                {
                    saveMsg.append(",").append(this.saveMsg[i]);
                }
            }
            mission.setSavemsg(saveMsg.toString());
            mission.setGroupid(this.groupID);
            mission.setStatus(this.status);
            mission.setGrouplist(this.groupList);
        }
        return mission;
    }

    /**
     * 获取提醒任务
     * @Title getRemindMission
     * @Description 获取提醒任务
     * @return 如果type不为1 则返回否则为null
     * @author 张逸辰
     * @Date 2020/9/14 20:36
     */
    public Remindmission getRemindMission(){
        Remindmission remindmission = null;
        if(this.type == 1){
            remindmission = new Remindmission();
            remindmission.setmId(this.M_ID);
            remindmission.setName(this.name);
            remindmission.setIsprivate(this.isPrivate);
            remindmission.setStarttime(this.startTime);
            remindmission.setEndtime(this.endTime);
            remindmission.setMsg(this.Msg);
            remindmission.setGroupid(this.groupID);
            remindmission.setStatus(this.status);
            remindmission.setGrouplist(this.groupList);
            remindmission.setIsrepeat(this.isRepeat);
            remindmission.setRepeattime(this.repeatTime);
        }
        return remindmission;
    }

    public String getM_ID() {
        return M_ID;
    }

    public void setM_ID(String m_ID) {
        M_ID = m_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getEndMsg() {
        return endMsg;
    }

    public void setEndMsg(String endMsg) {
        this.endMsg = endMsg;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getRegex() {
        return Regex;
    }

    public void setRegex(String regex) {
        Regex = regex;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public String getRemindMsg() {
        return remindMsg;
    }

    public void setRemindMsg(String remindMsg) {
        this.remindMsg = remindMsg;
    }

    public Boolean getRemindIsPrivate() {
        return remindIsPrivate;
    }

    public void setRemindIsPrivate(Boolean remindIsPrivate) {
        this.remindIsPrivate = remindIsPrivate;
    }

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public String[] getSaveMsg() {
        return saveMsg;
    }

    public void setSaveMsg(String[] saveMsg) {
        this.saveMsg = saveMsg;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }

    public Boolean getRepeat() {
        return isRepeat;
    }

    public void setRepeat(Boolean repeat) {
        isRepeat = repeat;
    }

    public Long getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(Long repeatTime) {
        this.repeatTime = repeatTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
