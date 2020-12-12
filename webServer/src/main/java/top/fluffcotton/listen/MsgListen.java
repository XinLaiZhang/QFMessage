package top.fluffcotton.listen;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.fluffcotton.annotation.BotHandle;
import top.fluffcotton.handle.Handle;
import top.fluffcotton.listen.handle.MsgHandle;
import top.fluffcotton.pojo.MatchResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: MsgListen
 * @Description: 消息处理监听器（私聊，讨论组，群）
 * @date 2020.07.21 21:06
 */
@BotHandle({FriendMessageEvent.class, TempMessageEvent.class, GroupMessageEvent.class})
@Component
public class MsgListen implements Handle {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MsgListen.class);
    /**
     * 消息统计处理器
     */
    private static final List<MsgHandle> MSG_HANDLE_LIST = new CopyOnWriteArrayList<>();

    private int count;
    /**
     * 用于处理统计任务监听器
     *
     * @param msg    消息
     * @Title msgHandle
     * @Description 用于处理统计任务监听器
     * @author 张逸辰
     * @Date 2020/7/21 21:08
     */
//    @Listen({MsgGetTypes.privateMsg, MsgGetTypes.discussMsg, MsgGetTypes.groupMsg})
//    public void msgHandle(MessageEvent msg) {
////        logger.info(Language.format("listen.MsgListen.msg"), type, msg);
//        //遍历处理器
//        for (MsgHandle mh : MSG_HANDLE_LIST) {
//            //消息类型分配处理匹配
//            Class<? extends MessageEvent>[] msgTypes = mh.getMsgTypes();
//            boolean msgType = false;
//            for (Class<? extends MessageEvent> mt : msgTypes) {
//                if (mt.equals(msg.getClass())) {
//                    msgType = true;
//                    break;
//                }
//            }
//            //验证是否被匹配
//            if (msgType && mh.matcher(msg)) {
////                logger.info(Language.format("listen.MsgListen.matcherSuccess"),mh.getName());
//                //执行成功处理
//                mh.execute(msg);
//            }
//        }
//    }
    /**
     * 注册消息统计器
     * @Title msgListenRegister
     * @Description 注册消息统计器
     * @param mh 消息统计器
     * @author 张逸辰
     * @Date 2020/7/22 16:33
     */
    public static void register(MsgHandle mh){
        MSG_HANDLE_LIST.add(mh);
    }
    /**
     * 删除统计任务
     * @Title deleteMission
     * @Description 删除统计任务
     * @param id 要删除的统计任务
     * @author 张逸辰
     * @Date 2020/8/31 16:55
     */
    public static void deleteMission(String id){
        MSG_HANDLE_LIST.removeIf(msgHandle -> msgHandle.getID().equals(id));
    }

    @Override
    public MatchResult match(BotEvent event) {
        MessageEvent msg = (MessageEvent) event;
        //遍历处理器
        for (MsgHandle mh : MSG_HANDLE_LIST) {
            //消息类型分配处理匹配
            Class<? extends MessageEvent>[] msgTypes = mh.getMsgTypes();
            boolean msgType = false;
            for (Class<? extends MessageEvent> mt : msgTypes) {
                if (mt.equals(msg.getClass())) {
                    msgType = true;
                    break;
                }
            }
            //验证是否被匹配
            if (msgType && mh.matcher(msg)) {
//                logger.info(Language.format("listen.MsgListen.matcherSuccess"),mh.getName());
                //执行成功处理
                mh.execute(msg);
                return MatchResult.SUCCESS;
            }
        }
        return MatchResult.FAIL;
    }

    @Override
    public void execute(BotEvent event) {

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
