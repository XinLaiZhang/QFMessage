package top.fluffcotton.listen;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.EmptyMessageChain;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import top.fluffcotton.listen.handle.MsgHandle;
import top.fluffcotton.mapper.MissionMapper;
import top.fluffcotton.pojo.Mission;
import top.fluffcotton.pojo.MissionExample;
import top.fluffcotton.pojo.MissionStatus;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.ExcelUtils;
import top.fluffcotton.service.TimeJob;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: CountMission
 * @Description: 统计任务
 * @date 2020.08.05 21:02
 */
@Component
@PropertySource("classpath:GroupTaskListConfig.properties")
public class CountMission {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CountMission.class);
    /**
     * 任务对应正则
     */
    private static final Map<String, Matcher> matcherMap = new HashMap<>(7);
    /**
     * 任务列表
     */
    private static List<Mission> missions;
    /**
     * excel处理工具
     */
    private final ExcelUtils excelUtils = ExcelUtils.getExcelUtils();
    /**
     * 正在启动的任务列表
     */
    private final Map<String, List<ScheduledFuture<?>>> countMissionList = new HashMap<>();
    /**
     * 任务列表mapper
     */
    @Autowired
    private MissionMapper missionMapper;
    /**
     * 文件夹名称
     */
    @Value("${groupTask.url}")
    private String groupTaskUrl;
    /**
     * 定时任务
     */
    @Autowired
    private TimeJob timeJob;
    /**
     * 机器人服务
     */
    @Autowired
    private BotService botService;

    /**
     * 初始化
     *
     * @Title init
     * @Description 初始化
     * @author 张逸辰
     * @Date 2020/8/6 20:55
     */
    @PostConstruct
    public void init() {
        //获取任务列表
        missions = missionMapper.selectByExample(new MissionExample());
        logger.info("获取统计任务列表{}", JSON.toJSONString(missions));
        for (Mission mission : missions) {
            register(mission, true);
        }
    }

    /**
     * 注册统计任务
     *
     * @param mission 统计任务
     * @param isInit
     * @Title register
     * @Description 注册统计任务
     * @author 张逸辰
     * @Date 2020/8/31 16:09
     */
    public void register(Mission mission, boolean isInit) {
        if (!isInit) {
            missions.add(mission);
        }
        logger.info("注册任务{}", JSON.toJSONString(mission));
        //注册任务开始
        registerStart(mission);
        if (mission.getRemindtime() != null && mission.getRemindtime() > 0) {
            //注册任务提醒
            registerRemind(mission);
        }
        //注册任务结束
        registerEnd(mission);
    }

    /**
     * 删除统计任务
     *
     * @param mission 任务
     * @Title delete
     * @Description 删除统计任务
     * @author 张逸辰
     * @Date 2020/8/31 17:46
     */
    public void delete(Mission mission) {
        for (Mission m : missions) {
            if (m.getmId().equals(mission.getmId())) {
                //删除全部提醒任务
                List<ScheduledFuture<?>> scheduledFutures = countMissionList.get(mission.getmId());
                if(mission.getStatus() < MissionStatus.FINISH.getByte() && scheduledFutures != null){
                    for (ScheduledFuture<?> scheduledFuture : scheduledFutures) {
                        if(scheduledFuture != null){
                            scheduledFuture.cancel(false);
                        }
                    }
                    countMissionList.remove(mission.getmId());
                }
                missions.remove(m);
                break;
            }
        }
    }


    /**
     * 注册任务开始
     *
     * @param mission 任务
     * @Title registerStart
     * @Description 注册任务开始
     * @author 张逸辰
     * @Date 2020/8/31 17:20
     */
    private void registerStart(Mission mission) {
        //判断收到的消息是否为私聊消息
        Boolean isPrivate = mission.getIsprivate();
        Group group = botService.getGroup(Long.parseLong(mission.getGroupid()));
        if(group == null){
            return;
        }
        //注册任务开始
        ScheduledFuture<?> future = timeJob.start(() -> {
            if (mission.getStatus().equals(MissionStatus.UNSTART.getByte())) {
                group.sendMessage(mission.getMsg());
                mission.setStatus(MissionStatus.PROGRESS.getByte());
                updateStatus(mission);
            }
            MsgListen.register(new MsgHandle() {

                @Override
                public Class<? extends MessageEvent>[] getMsgTypes() {
                    return new Class[]{isPrivate ? FriendMessageEvent.class : GroupMessageEvent.class};
                }

                @Override
                public boolean matcher(MessageEvent msg) {
                    PlainText text = msg.getMessage().first(PlainText.Key);
                    if(text == null){
                        return false;
                    }
                    //编译正则
                    Matcher matcher = Pattern.compile(mission.getRegex()).matcher(text.getContent().trim());
                    boolean matches = matcher.matches();
                    if (matches) {
                        matcherMap.put(mission.getmId(), matcher);
                    }
                    return matches;
                }

                @Override
                public boolean execute(MessageEvent msg, Object... args) {
                    try {
                        //获取储存内容
                        String saveMsg = mission.getSavemsg();
                        //获取列表名单地址
                        String fileUrl = excelUtils.getGroupTaskListURL() + File.separator + groupTaskUrl + mission.getGrouplist();
                        List<Map<Integer, String>> userList = excelUtils.getUserList(mission.getmId(), fileUrl);
                        Matcher matcher = matcherMap.get(mission.getmId());
                        //取匹配主键，为qq号
                        String primaryKey;
                        if (isPrivate) {
                            primaryKey = String.valueOf(msg.getSender().getId());
                        } else {
                            primaryKey = String.valueOf(msg.getSender().getId());
                        }
                        //获取发出消息用户，并且标记
                        for (Map<Integer, String> user : userList) {
                            if (user.get(ExcelUtils.PRIMARY_KEY).equals(primaryKey)) {
                                //标记
                                user.putIfAbsent(ExcelUtils.FINISH_FLAG, String.valueOf(true));
                                //如果保存消息
                                if (!"".equals(saveMsg)) {
                                    String[] saveMsgList = saveMsg.split(",");
                                    //存保存数据
                                    for (String index : saveMsgList) {
                                        user.put(ExcelUtils.FINISH_FLAG + Integer.parseInt(index) + 1, matcher.group(Integer.parseInt(index) + 1));
                                    }
                                }
                            }
                            logger.info("更新用户信息{}", JSON.toJSONString(user));
                            break;
                        }
                        //保存数据到excel
                        EasyExcel.write(fileUrl).head(excelUtils.getHeadList()).sheet().doWrite(excelUtils.getExcelDataList(userList));
                        //回复消息
                        if (!"".equals(mission.getReply())) {
                            if (isPrivate) {
                                msg.getSender().sendMessage(mission.getReply());
                            } else {
                                //获取@
                                ((GroupMessageEvent) msg).getGroup().sendMessage(new At(((GroupMessageEvent) msg).getSender()).plus(mission.getReply()));
                            }
                        }
                    } catch (Exception e) {
                        logger.error("异常错误", e);
                        return false;
                    }
                    return true;
                }

                @Override
                public String getName() {
                    return "统计任务" + mission.getmId();
                }

                @Override
                public String getID() {
                    return String.valueOf(mission.getmId());
                }
            });
        }, mission.getStarttime(), 0);
        addFuture(mission, future);
    }


    private void updateStatus(Mission mission) {
//        mission.setStatus(MissionStatus.PROGRESS.getByte());
        missionMapper.updateByPrimaryKey(mission);
    }

    /**
     * 注册任务提醒
     *
     * @param mission 任务
     * @Title registerRemind
     * @Description 注册任务提醒
     * @author 张逸辰
     * @Date 2020/8/31 17:20
     */
    private void registerRemind(Mission mission) {
        Group group = botService.getGroup(Long.parseLong(mission.getGroupid()));
        if(group == null){
            return;
        }
        ScheduledFuture<?> future = timeJob.start(() -> {
            //获取列表名单地址
            String fileUrl = excelUtils.getGroupTaskListURL() + File.separator + groupTaskUrl + mission.getGrouplist();
            //提醒是否为私聊
            Boolean remindIsPrivate = mission.getRemindisprivate();
            List<Map<Integer, String>> userList = excelUtils.getUserList(mission.getmId(), fileUrl);
            String remindMsg = mission.getRemindmsg();
            if (remindIsPrivate) {
                //给未完成的发送 未完成消息
                for (Map<Integer, String> user : userList) {
                    if (user.get(ExcelUtils.FINISH_FLAG) == null || user.get(ExcelUtils.FINISH_FLAG).equals(String.valueOf(false))) {
                        try{
                            Member member = group.get(Long.parseLong(user.get(ExcelUtils.PRIMARY_KEY)));
                            member.sendMessage(remindMsg);
                        }catch (NoSuchElementException e){
                            //TODO 日志
                        }
                    }
                }
            } else {
                //生成需要提醒的人
                MessageChain atCQ = EmptyMessageChain.INSTANCE;
                for (Map<Integer, String> user : userList) {
                    if (user.get(ExcelUtils.FINISH_FLAG) == null || user.get(ExcelUtils.FINISH_FLAG).equals(String.valueOf(false))) {
                        try{
                            Member member = group.get(Long.parseLong(user.get(ExcelUtils.PRIMARY_KEY)));
                            atCQ.plus(new At(member));
                        }catch (NoSuchElementException e){
                            //TODO 日志
                        }
                    }
                }
                group.sendMessage(atCQ + remindMsg);
            }
        }, mission.getEndtime() - mission.getRemindtime() * 60 * 1000, 0);
        addFuture(mission, future);
    }

    /**
     * 注册任务结束
     *
     * @param mission 任务
     * @Title registerEnd
     * @Description 注册任务结束
     * @author 张逸辰
     * @Date 2020/8/31 17:21
     */
    private void registerEnd(Mission mission) {
        Group group = botService.getGroup(Long.parseLong(mission.getGroupid()));
        if(group == null){
            return;
        }
        ScheduledFuture<?> future = timeJob.start(() -> {
            if (mission.getStatus().equals(MissionStatus.PROGRESS.getByte())) {
                //发送任务结束消息
                group.sendMessage(mission.getEndmsg());
                MsgListen.deleteMission(String.valueOf(mission.getmId()));
                mission.setStatus(MissionStatus.FINISH.getByte());
                updateStatus(mission);
                delete(mission);
            }
        },mission.getEndtime(), 0);
        addFuture(mission, future);
    }

    /**
     * 获取用户列表
     *
     * @param mission 任务
     * @return 用户名单
     * @Title getUserList
     * @Description 获取用户列表
     * @author 张逸辰
     * @Date 2020/8/31 21:59
     */
    public List<Map<Integer, String>> getUserList(Mission mission) {
        String fileUrl = excelUtils.getGroupTaskListURL() + File.separator + groupTaskUrl + mission.getGrouplist();
        return excelUtils.getUserList(mission.getmId(), fileUrl);
    }

    /**
     * 将任务得未来放入列表
     * @param mission 任务
     * @param future 未来
     */
    private void addFuture(Mission mission, ScheduledFuture<?> future) {
        if(countMissionList.containsKey(mission.getmId())){
            List<ScheduledFuture<?>> scheduledFutures = countMissionList.get(mission.getmId());
            scheduledFutures.add(future);
        }else{
            List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
            scheduledFutures.add(future);
            countMissionList.put(mission.getmId(),scheduledFutures);
        }
    }
}
