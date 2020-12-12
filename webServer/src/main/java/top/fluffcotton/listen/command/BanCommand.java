package top.fluffcotton.listen.command;


import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import top.fluffcotton.listen.handle.CommandHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: BanCommand
 * @Description: 全群禁言命令
 * 指令：开启禁言 --开启禁言
 * 关闭禁言 --关闭禁言
 * 禁言@qq 123 单位分钟
 * @date 2020.07.19 17:11
 */
public class BanCommand implements CommandHandle {

    /**
     * 开启禁言指令匹配正则
     */
    private static final Pattern ON_PATTERN = Pattern.compile("开启禁言");
    /**
     * 关闭禁言指令匹配正则
     */
    private static final Pattern OFF_PATTERN = Pattern.compile("关闭禁言");

    /**
     * 禁言指定成员 指定时间
     */
    private static final Pattern BAN_PATTERN = Pattern.compile("^禁言$");
    /**
     * 解除禁言
     */
    private static final Pattern OFF_BAN_PATTERN = Pattern.compile("^解禁$");

    /**
     * 执行禁言处理，200 为开启全群禁言，201关闭全群禁言，202禁言成员成功，203解禁成员成功
     *
     * @param msg     消息
     * @param content 匹配
     * @return 执行状态
     * @Title execute
     * @Description 执行禁言处理
     * @author 张逸辰
     * @Date 2020/7/19 18:04
     */
    @Override
    public long execute(MessageEvent msg, Map<String, String> content) {
        //开启
        if (content.get("type").equals(ON_PATTERN.pattern())) {
            try {
            ((GroupMessageEvent)msg).getGroup().getSettings().setMuteAll(true);
                return 200;
            }catch (PermissionDeniedException e){
                return 0;
            }
        } else if (content.get("type").equals(OFF_PATTERN.pattern())) {
            //关闭
            try {
                ((GroupMessageEvent)msg).getGroup().getSettings().setMuteAll(false);
                return 201;
            }catch (PermissionDeniedException e){
                return 0;
            }
        } else if (content.get("type").equals(BAN_PATTERN.pattern())) {
            //禁言某人
            try {
                ((GroupMessageEvent)msg).getGroup().getMembers().get(Long.parseLong(content.get("qq"))).mute(Integer.parseInt(content.get("time")) * 60);
                return Long.parseLong(content.get("qq") + 202);
            }catch (PermissionDeniedException e){
                return 0;
            }
        } else if (content.get("type").equals(OFF_BAN_PATTERN.pattern())) {
            //解禁某人
            try {
                ((GroupMessageEvent)msg).getGroup().getMembers().get(Long.parseLong(content.get("qq"))).unmute();
                return Long.parseLong(content.get("qq") + 203);
            }catch (PermissionDeniedException e){
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Map<String, String> match(MessageEvent msg) {
        MessageChain message = msg.getMessage();
        PlainText text = message.first(PlainText.Key);
        if(text == null){
            return null;
        }
        Map<String,String> map = new HashMap<>(3);
        //开启指令
        Matcher onMatcher = ON_PATTERN.matcher(text.getContent().trim());
        if (onMatcher.matches()) {
            map.put("type", ON_PATTERN.pattern());
            return map;
        }
        //关闭指令
        Matcher offMatcher = OFF_PATTERN.matcher(text.getContent().trim());
        if (offMatcher.matches()) {
            map.put("type", OFF_PATTERN.pattern());
            return map;
        }
        At at = message.first(At.Key);
        if(at == null){
            return null;
        }
        map.put("qq", String.valueOf(at.getTarget()));
        //解禁某人
        Matcher offBanMatcher = OFF_BAN_PATTERN.matcher(text.getContent().trim());
        if (offBanMatcher.matches()) {
            map.put("type", OFF_BAN_PATTERN.pattern());
            return map;
        }
        List<PlainText> texts = new ArrayList<>();
        for (SingleMessage singleMessage : message) {
            if(singleMessage instanceof PlainText){
                texts.add((PlainText) singleMessage);
            }
        }
        if(texts.size() != 2){
            return null;
        }
        try {
            PlainText plainText = texts.get(1);
            long l = Long.parseLong(plainText.getContent().trim());
            map.put("time", String.valueOf(l));
        }catch (Exception e){
            return null;
        }
        //禁言某人
        Matcher banMatcher = BAN_PATTERN.matcher(text.getContent().trim());
        if (banMatcher.matches()) {
            map.put("type", BAN_PATTERN.pattern());
            return map;
        }
        return null;
    }

    @Override
    public boolean isHasPower(MessageEvent msg, Member commander, Map<String, String> content) {
        return commander.getPermission().equals(MemberPermission.OWNER) || commander.getPermission().equals(MemberPermission.ADMINISTRATOR);
    }

    @Override
    public String getSuccessMsg(long code) {
        String codeStr = String.valueOf(code);
        int exCode;
        String msg = null;
        if(codeStr.length() > 3){
//            String qq = codeStr.substring(0,codeStr.length() - 3);
            exCode = Integer.parseInt(codeStr.substring(codeStr.length() - 3));
            switch (exCode){
                case 202:msg = "恭喜您喜提禁言套餐一份~";break;
                case 203:msg = "抱歉哦，是人家搞错了~";break;
            }
        }else
        {
            exCode = Integer.parseInt(codeStr);
            switch (exCode){
                case 200: msg = "开启全群禁言，群友们该休息了哦~";break;
                case 201:msg = "关闭全群禁言，群友们粗来聊天啦~";break;
            }
        }
        return msg;
    }

    @Override
    public String getFailMsg(long code, String msg) {
        return "禁言失败";
    }

    @Override
    public String getHelp() {
        return "指令：开启禁言/关闭禁言"+System.lineSeparator()+"禁言@成员 [禁言时间，单位为分钟]"+System.lineSeparator()+"例：禁言@张逸辰 100"+System.lineSeparator()+"解禁@成员"+System.lineSeparator()+"例：解禁@张逸辰";
    }

    @Override
    public String getName() {
        return "全群禁言";
    }

    @Override
    public String getID() {
        return "1";
    }
}
