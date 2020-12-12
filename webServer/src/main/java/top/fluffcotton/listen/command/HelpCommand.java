package top.fluffcotton.listen.command;


import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.fluffcotton.listen.CommandListen;
import top.fluffcotton.listen.handle.CommandHandle;
import top.fluffcotton.pojo.CommandVo;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.service.GroupSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: HelpCommand
 * @Description: 帮助提示命令，指令：@机器人 帮助
 * @date 2020.07.20 17:24
 */
@Component
public class HelpCommand implements CommandHandle {

    /**
     * 获取指令列表，有指令
     */
    public static final int HAS_COMMAND = 200;
    /**
     * 获取指令列表，无指令
     */
    public static final int NOT_COMMAND = 201;
    /**
     * 指令匹配正则
     */
    private static final Pattern PATTERN = Pattern.compile("^帮助$");
    /**设置*/
    @Autowired
    private GroupSetting groupSetting;

    @Override
    public long execute(MessageEvent msg, Map<String, String> content) {
        List<CommandHandle> commandHandleList = CommandListen.getCommandHandleList();
        //指令内无指令
        if (commandHandleList.size() <= 0) {
            return NOT_COMMAND;
        }
        //有指令
        return Long.parseLong(String.valueOf(((GroupMessageEvent)msg).getGroup().getId()) + HAS_COMMAND);
    }

    @Override
    public Map<String, String> match(MessageEvent msg) {
        MessageChain message = msg.getMessage();
        At at = message.first(At.Key);
        if(at == null || at.getTarget() != msg.getBot().getId()){
            return null;
        }
        PlainText text = message.first(PlainText.Key);
        if(text == null){
            return null;
        }
        Matcher matcher = PATTERN.matcher(text.getContent().trim());
        //@qq和当前处理qq一致，并且匹配
        if (matcher.matches()) {
            return new HashMap<>();
        }
        return null;
    }

    @Override
    public boolean isHasPower(MessageEvent msg, Member commander, Map<String, String> content) {
        return true;
    }

    @Override
    public String getSuccessMsg(long code) {
        String codeStr = String.valueOf(code);
        String groupID = codeStr.substring(0, codeStr.length() - 3);
        int exCode = Integer.parseInt(codeStr.substring(codeStr.length() - 3));
        if (exCode == HAS_COMMAND) {
            StringBuilder msg = new StringBuilder("小辰当前可用指令有：");
            List<CommandHandle> commandHandleList = CommandListen.getCommandHandleList();
            QqGroup qqGroup = groupSetting.selectByGroupId(groupID, false);
            CommandVo commandVo = groupSetting.selectCommandVo(qqGroup);
            //获取该群命令列表
            List<String> commandList = commandVo.getCommands();
            for (CommandHandle ch : commandHandleList) {
                //获取群开启的命令文档
                boolean isHas = false;
                for(String command:commandList){
                    if(command.equals(ch.getID())){
                        isHas = true;
                        break;
                    }
                }
                if(isHas){
                    msg.append(System.lineSeparator()).append("【").append(ch.getName()).append("】 : ").append(ch.getHelp());
                }
            }
            msg.append("以上是小辰全部可用指令哦~");
            return msg.toString();
        } else if (exCode == NOT_COMMAND) {
            return "当前没有可用的指令哦~";
        }
        return null;
    }

    @Override
    public String getFailMsg(long code, String msg) {
        return "抱歉哦~，小辰遇到了问题，无法打开帮助文档。";
    }


    @Override
    public String getHelp() {
        return "指令：@机器人 帮助";
    }

    @Override
    public String getName() {
        return "帮助";
    }

    @Override
    public String getID() {
        return "0";
    }
}
