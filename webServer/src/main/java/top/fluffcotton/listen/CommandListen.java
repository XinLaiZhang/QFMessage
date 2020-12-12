package top.fluffcotton.listen;


import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.fluffcotton.annotation.BotHandle;
import top.fluffcotton.handle.Handle;
import top.fluffcotton.listen.command.BanCommand;
import top.fluffcotton.listen.command.CardCommand;
import top.fluffcotton.listen.command.HelpCommand;
import top.fluffcotton.listen.handle.CommandHandle;
import top.fluffcotton.pojo.CommandVo;
import top.fluffcotton.pojo.MatchResult;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.service.GroupSetting;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: CommandListen
 * @Description: 口令执行器
 * @date 2020.07.16 14:42
 */
//@BotHandle({GroupMessageEvent.class, TempMessageEvent.class, FriendMessageEvent.class})
@Component
@BotHandle({GroupMessageEvent.class})
public class CommandListen implements Handle {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CommandListen.class);
    /**
     * 口令处理器 线程不安全
     */
    private static final List<CommandHandle> COMMAND_HANDLE_LIST = new CopyOnWriteArrayList<>();
    /**
     * 设置
     */
    private final GroupSetting groupSetting;
    private final HelpCommand helpCommand;
    /**
     * 执行次数
     */
    private int count;


    public CommandListen(GroupSetting groupSetting, HelpCommand helpCommand){
        CommandListen.register(new BanCommand());
        CommandListen.register(new CardCommand());
        this.groupSetting = groupSetting;
        this.helpCommand = helpCommand;
        CommandListen.register(helpCommand);
    }

    /**
     * 指令注册，要求返回的name 唯一
     *
     * @param ch 口令任务
     * @Title commandRegister
     * @Description 添加任务
     * @author 张逸辰
     * @Date 2020/7/16 15:17
     */
    public static void register(CommandHandle ch) {
        //验证处理器名称唯一
        for (CommandHandle commandHandle : COMMAND_HANDLE_LIST) {
            if (commandHandle.getID().equals(ch.getID())) {
                logger.error("注册{}失败，存在相同id处理器{}", ch, commandHandle);
                return;
            }
        }
        CommandListen.COMMAND_HANDLE_LIST.add(ch);
    }

    /**
     * 获取口令处理器  请勿直接操作 慎用！！！  当前存在线程安全问题
     *
     * @return 口令处理器
     * @Title getCommandHandleList
     * @Description 获取口令处理器
     * @author 张逸辰
     * @Date 2020/7/20 17:33
     */
    public static List<CommandHandle> getCommandHandleList() {
        return COMMAND_HANDLE_LIST;
    }

    /**
     * 验证是否有权限执行命令
     *
     * @param commander 命令者
     * @param cv        群对应的权限
     * @param ch        命令处理器
     * @return 成功//失败
     * @Title isHasPower
     * @Description 权限验证
     * @author 张逸辰
     * @Date 2020/7/16 15:03
     */
    public boolean isHasPower(Member commander, CommandVo cv, CommandHandle ch) {
        List<String> commandList = cv.getCommands();
        boolean isPower = false;
        //匹配命令列表内是否存在当前命令处理器
        for (int i = 0; i < commandList.size(); i++) {
            if (commandList.get(i).equals(ch.getID())) {
                //获取权限
                List<String> powerList = cv.getPowers().get(i);
                for (String power : powerList) {
                    //群主
                    if ("1".equals(power)) {
                        isPower = commander.getPermission().equals(MemberPermission.OWNER);
                        //管理员
                    } else if ("0".equals(power)) {
                        isPower = commander.getPermission().equals(MemberPermission.ADMINISTRATOR);
                        //QQ号
                    } else {
                        isPower = commander.getId() == Long.parseLong(power);
                    }
                    if (isPower) {
                        break;
                    }
                }
            }
        }
        return isPower;
    }

    @Override
    public MatchResult match(BotEvent event) {
        GroupMessageEvent msg;
        if(event instanceof GroupMessageEvent){
            msg = (GroupMessageEvent) event;
        }else {
            return MatchResult.FAIL;
        }
        QqGroup qqGroup = groupSetting.selectByGroupId(String.valueOf(msg.getGroup().getId()), false);
        //匹配群是否在命令列表内
        if (qqGroup == null) {
            return MatchResult.FAIL;
        }
        if (!groupSetting.openCommand(qqGroup)) {
            return MatchResult.FAIL;
        }

        return MatchResult.SUCCESS;
    }

    @Override
    public void execute(BotEvent event) {
        GroupMessageEvent msg = (GroupMessageEvent) event;
        assert msg != null;
        QqGroup qqGroup = groupSetting.selectByGroupId(String.valueOf(msg.getGroup().getId()), false);
        assert qqGroup != null;
        //从命令列表获取匹配信息，进行处理
        for (CommandHandle ch : COMMAND_HANDLE_LIST) {
            Map<String,String> content = null;
            //获取参数
            content = ch.match(msg);
            //验证
            if (content != null) {
//                logger.info(Language.format("listen.CommandListen.commandListen.matcherSuccess"), ch.getName());
                //获取命令者
                Member commander = msg.getSender();
                String failMsg;
                //验证权限
                CommandVo commandVo = groupSetting.selectCommandVo(qqGroup);
                if (isHasPower(commander, commandVo, ch)) { //&& ch.isHasPower(msg, commander, matcher)
                    long code = ch.execute(msg, content);
                    if (code > 0) {
//                        logger.info(Language.format("listen.CommandListen.commandListen.success"), ch.getName(), commander.getQQ(), commander.getNickname());
                        if (ch.getSuccessMsg(code) != null) {
                            msg.getGroup().sendMessage(ch.getSuccessMsg(code));
                        }
                        return;
                    } else {
                        failMsg = "执行失败";
//                        failMsg = Language.format("listen.CommandListen.commandListen.reason.fail");
                        if (ch.getFailMsg(code, failMsg) != null) {
                            msg.getGroup().sendMessage(ch.getFailMsg(code, failMsg));
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}






