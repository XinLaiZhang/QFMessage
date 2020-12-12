package top.fluffcotton.listen;


import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.fluffcotton.mapper.GroupFunctionMapper;
import top.fluffcotton.pojo.GroupFunction;
import top.fluffcotton.pojo.GroupFunctionExample;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author : 张逸辰
 * @Title : CheckMessage
 * @Description :检测消息的合法性，非法消息进行撤回
 * @date : 2020年7月5日 下午4:16:22
 */
@Component
public class CheckMessageImp implements CheckMessage {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CheckMessageImp.class);
    /**
     * 屏蔽关键词
     */
    private static List<String> bannerMsgList;
    /**
     * GroupFunctionMapper
     */
    private final GroupFunctionMapper gfm;

    public CheckMessageImp(GroupFunctionMapper gfm) {
        this.gfm = gfm;
    }


    /**
     * 通过全局最大优先级强制处理全部群聊，讨论组消息，自动屏蔽非法消息
     *
     * @param msg    消息
     * @Title checkMsg
     * @Description 消息合法性审核
     * @author 张逸辰
     * @Date 2020/7/14 16:28
     */
//    @Listen(value = {MsgGetTypes.discussMsg, MsgGetTypes.groupMsg}, sort = Integer.MAX_VALUE)
    public void checkMsg(MessageEvent msg) {
//        logger.info(Language.format("listen.CheckMessage.bannerMsgHandle.msg"), types, msg);
        //初始化枷锁
        if (bannerMsgList == null) {
            synchronized (CheckMessageImp.class) {
                initBannerMsgList();
            }
        }
        //判断是否操作的为禁用词
        if (isBannerMsg(msg)) {
//            logger.info(Language.format("listen.CheckMessage.bannerMsgHandle.isBanner"), msg);
            bannerMsgHandle(msg);
        }
    }

    /**
     * 初始化bannerMsgList
     *
     * @Title initBannerMsgList
     * @Description 初始化bannerMsgList
     * @author 张逸辰
     * @Date 2020/7/15 14:53
     */
    @PostConstruct
    private void initBannerMsgList() {
        logger.info("初始化bannerMsgList");
        bannerMsgList = new CopyOnWriteArrayList<>();
        //获取禁用词
        File bannerMsg = new File(Objects.requireNonNull(CheckMessageImp.class.getClassLoader().getResource("")).getPath() + "/bannerMsg");
        logger.info("获取bannerMsg文件:{}", bannerMsg);
        //判断文件是否存在
        if (bannerMsg.exists()) {
            try {
                logger.info("读取文本内容");
                BufferedReader bannerMsgReader = new BufferedReader(new InputStreamReader(new FileInputStream(bannerMsg)));
                String bufferString;
                while ((bufferString = bannerMsgReader.readLine()) != null) {
                    bannerMsgList.add(bufferString);
                }
                logger.info("获取禁用关键字：{}", bannerMsgList);
            } catch (FileNotFoundException e) {
                logger.error("bannerMsg文件不存在", e);
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("读取bannerMsg失败", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断 在管理列表内的群消息是否为违规消息
     *
     * @param msg 是返回true
     * @return 是否违规
     * @Title isBannerMsg
     * @Description 判断消息是否为违规消息
     * @author 张逸辰
     * @Date 2020/7/15 13:35
     */
    @Override
    public boolean isBannerMsg(MessageEvent msg) {
        Group groupNum = null;
        //获取群号
        if (msg instanceof GroupMessageEvent) {
            groupNum = ((GroupMessageEvent) msg).getGroup();
        } else if (msg instanceof TempMessageEvent) {
            groupNum = ((TempMessageEvent) msg).getGroup();
        }
        //是否收到的消息是否存在管理列表内
        assert groupNum != null;
        if (isInnerCheckGroupList(String.valueOf(groupNum.getId()))) {
            //如果在禁用词汇列表则返回true
            for (String s : bannerMsgList) {
                if (msg.getMessage().toString().contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断群是否在管理列表
     *
     * @param groupNum 群号
     * @return 是否在消息审核列表
     * @Title isInnerCheckGroupList
     * @Description 判断群是否在管理列表
     * @author 张逸辰
     * @Date 2020/7/15 14:49
     */
    @Override
    public boolean isInnerCheckGroupList(String groupNum) {
        List<GroupFunction> groupFunctions = gfm.selectByExample(new GroupFunctionExample());
        for (GroupFunction gf : groupFunctions) {
            if (gf.getGroupid().compareTo(groupNum) == 0 && gf.getCheckmsg()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用来处理禁用词的操作
     *
     * @param msg    消息
     * @Title bannerMsgHandle
     * @Description 处理禁用词的方法
     * @author 张逸辰
     * @Date 2020/7/15 13:37
     */
    @Override
    public void bannerMsgHandle(MessageEvent msg) {
        try {
            msg.getBot().recall(msg.getMessage());
//                logger.info(Language.format("listen.CheckMessage.bannerMsgHandle.recallSuccess"), msg.getMsg());
        } catch (PermissionDeniedException e) {
//                logger.info(Language.format("listen.CheckMessage.bannerMsgHandle.recallFail"));
        }
        //发送提示信息
        //检测消息类型
        if (msg instanceof GroupMessageEvent) {
            ((GroupMessageEvent)msg).getGroup().sendMessage("请规范您的消息中存在非法词汇");
        } else if (msg instanceof TempMessageEvent) {
            ((TempMessageEvent)msg).getGroup().sendMessage("请规范您的消息中存在非法词汇");
        }
    }
}
