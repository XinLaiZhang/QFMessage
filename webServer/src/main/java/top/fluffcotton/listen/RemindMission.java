package top.fluffcotton.listen;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import top.fluffcotton.mapper.RemindmissionMapper;
import top.fluffcotton.pojo.MissionStatus;
import top.fluffcotton.pojo.Remindmission;
import top.fluffcotton.pojo.RemindmissionExample;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.ExcelUtils;
import top.fluffcotton.service.TimeJob;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: RemindMission
 * @Description: 提醒任务
 * @date 2020.08.02 21:17
 */
@Component
@PropertySource("classpath:GroupTaskListConfig.properties")
public class RemindMission {

    /**
     * 任务列表
     */
    private static final List<Remindmission> remindMissions = new CopyOnWriteArrayList<>();
    /**
     * excel处理工具
     */
    private final ExcelUtils excelUtils = ExcelUtils.getExcelUtils();
    /**
     * 正在启动的任务列表
     */
    private final Map<String,ScheduledFuture<?>> remindMissionList = new HashMap<>();
    /**
     * 提醒任务mapper
     */
    @Autowired
    private RemindmissionMapper remindmissionMapper;
    @Autowired
    private TimeJob timeJob;
    /**
     * 机器人服务
     */
    @Autowired
    private BotService botService;
    /**
     * 文件夹名称
     */
    @Value("${groupTask.url}")
    private String groupTaskUrl;

    /**
     * 初始化
     *
     * @Title init
     * @Description 初始化
     * @author 张逸辰
     * @Date 2020/8/12 22:43
     */
    @PostConstruct
    public void init() {
        //获取任务列表
        RemindmissionExample remindmissionExample = new RemindmissionExample();
        remindmissionExample.createCriteria().andStatusEqualTo(MissionStatus.UNSTART.getByte());
        List<Remindmission> remindMissions = remindmissionMapper.selectByExample(remindmissionExample);
        //添加计时任务
        for (Remindmission remindMission : remindMissions) {
            register(remindMission);
        }
    }

    /**
     * 注册提醒任务
     *
     * @param remindMission 提醒任务
     * @Title register
     * @Description 注册提醒任务
     * @author 张逸辰
     * @Date 2020/8/31 16:08
     */
    public void register(Remindmission remindMission) {
        if (remindMission.getStarttime() != null && remindMission.getStatus()<=(MissionStatus.PROGRESS.getByte())) {
            remindMissions.add(remindMission);
            //获取任务是否重复
            Boolean isRepeat = remindMission.getIsrepeat();
            //是私聊消息吗
            Boolean isPrivate = remindMission.getIsprivate();
            Group group = botService.getGroup(Long.parseLong(remindMission.getGroupid()));
            if(group == null){
                return;
            }
            //注册任务
            ScheduledFuture<?> future = timeJob.start(() -> {
                if (isPrivate) {
                    //@好友列表格式  986,6456132
                    String fileUrl = excelUtils.getGroupTaskListURL() + File.separator + groupTaskUrl + remindMission.getGrouplist();
                    List<Map<Integer, String>> userList = excelUtils.getUserList(remindMission.getGroupid(), fileUrl);
                    if (userList != null && userList.size() > 0) {
                        for (Map<Integer, String> user : userList) {
                            try{
                                Member member = group.get(Long.parseLong(user.get(ExcelUtils.PRIMARY_KEY)));
                                member.sendMessage(remindMission.getMsg());
                            }catch (NoSuchElementException e){
                                //TODO 日志
                            }
                        }
                    }else{
                        //TODO 打印异常
                    }
                } else {
                    group.sendMessage(remindMission.getMsg());
                }
                //更新开始时间
                if(isRepeat){
                    updateStartTime(remindMission.getmId(),System.currentTimeMillis() + remindMission.getRepeattime());
                    updateStatus(remindMission.getmId(),MissionStatus.PROGRESS);
                }else{
                    updateStatus(remindMission.getmId(),MissionStatus.FINISH);
                    remindMissions.remove(remindMission);
                }
            }, remindMission.getStarttime(), isRepeat ? remindMission.getRepeattime()*1000 : 0);
            //截止时间关闭
            if(isRepeat){
                timeJob.start(()->{future.cancel(false);remindMissions.remove(remindMission);},remindMission.getEndtime(),0);
            }
            remindMissionList.put(remindMission.getmId(),future);
        }
    }

    /**
     * 删除提醒
     *
     * @param remindmission 提醒任务
     * @Title delete
     * @Description 删除提醒
     * @author 张逸辰
     * @Date 2020/8/31 20:12
     */
    public void delete(Remindmission remindmission) {
        remindMissions.removeIf(r -> r.getmId().equals(remindmission.getmId()));
        ScheduledFuture<?> scheduledFuture = remindMissionList.get(remindmission.getmId());
        if(remindmission.getStatus() < MissionStatus.FINISH.getByte() && scheduledFuture != null){
            scheduledFuture.cancel(false);
            remindMissionList.remove(remindmission.getmId());
        }
    }

    /**
     * 更新任务开始时间
     *
     * @param id   任务id
     * @param time 任务开始时间
     * @Title updateStartTime
     * @Description 更新任务时间
     * @author 张逸辰
     * @Date 2020/8/5 20:24
     */
    public void updateStartTime(String id, long time) {
        for (Remindmission rm : remindMissions) {
            if (id.equals(rm.getmId())) {
                rm.setStarttime(time);
                remindmissionMapper.updateByPrimaryKey(rm);
                break;
            }
        }
    }

    /**
     * 更新任务状态，同时删除提醒任务
     *
     * @param id     任务id
     * @param status 状态
     * @Title updateStatus
     * @Description 更新任务状态
     * @author 张逸辰
     * @Date 2020/8/5 20:25
     */
    public void updateStatus(String id, MissionStatus status) {
        for (Remindmission rm : remindMissions) {
            if (id.equals(rm.getmId())) {
                rm.setStatus(status.getByte());
                remindmissionMapper.updateByPrimaryKey(rm);
                remindMissions.remove(rm);
                break;
            }
        }
    }


}
