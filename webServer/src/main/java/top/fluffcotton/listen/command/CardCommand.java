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
 * @Title: CardCommand
 * @Description: 修改群名片
 * 格式: 改@qq [修改后名片]
 * @date 2020.07.16 16:41
 */
public class CardCommand implements CommandHandle {
    /**
     * 指令匹配正则
     */
    private static final Pattern PATTERN = Pattern.compile("^改$");

    /**
     * 执行修改群名片，若200为成功， 0为失败
     * @Title execute
     * @Description 修改群名片
     * @param msg 消息
     * @param content 正则匹配器
     * @return 成功状态
     * @author 张逸辰
     * @Date 2020/7/19 18:01
     */
    @Override
    public long execute(MessageEvent msg, Map<String, String> content) {
        try {
            ((GroupMessageEvent)msg).getGroup().getMembers().get(Long.parseLong(content.get("qq"))).setNameCard(content.get("newName"));
            return 200;
        }catch (PermissionDeniedException e){
            return 0;
        }
    }

    @Override
    public Map<String, String> match(MessageEvent msg) {
        MessageChain message = msg.getMessage();
        At at = message.first(At.Key);
        if(at == null){
            return null;
        }
        PlainText text = message.first(PlainText.Key);
        if(text == null){
            return null;
        }
        Matcher matcher = PATTERN.matcher(text.getContent().trim());
        if (matcher.matches()) {
            List<PlainText> texts = new ArrayList<>(2);
            for (SingleMessage singleMessage : message) {
                if(singleMessage instanceof PlainText){
                    texts.add((PlainText) singleMessage);
                }
            }
            if(texts.size() != 2){
                return null;
            }
            Map<String,String> map = new HashMap<>(2);
            map.put("newName",texts.get(1).getContent().trim());
            map.put("qq",String.valueOf(at.getTarget()));
            return map;
        }
        return null;
    }

    @Override
    public boolean isHasPower(MessageEvent msg, Member commander, Map<String, String> content) {
        //为群主，管理员，或者自己修改自己
        return commander.getPermission().equals(MemberPermission.OWNER) || commander.getPermission().equals(MemberPermission.ADMINISTRATOR) || commander.getId() == Long.parseLong(content.get("qq"));
    }

    @Override
    public String getName() {
        return "修改群名片";
    }

    @Override
    public String getID() {
        return "2";
    }

    @Override
    public String getSuccessMsg(long code) {
        return "修改成功";
    }

    @Override
    public String getFailMsg(long code, String msg) {
        return "修改失败："+ msg;
    }


    @Override
    public String getHelp() {
        return "改@成员[要改的名字] 例：改@张逸辰 123";
    }
}
