package top.fluffcotton.listen;

import net.mamoe.mirai.message.MessageEvent;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title:
 * @Description: 消息处理器
 * @date
 */
public interface CheckMessage {

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
    boolean isBannerMsg(MessageEvent msg) ;

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
    boolean isInnerCheckGroupList(String groupNum);

    /**
     * 用来处理禁用词的操作
     *
     * @param msg 消息事件
     * @Title bannerMsgHandle
     * @Description 处理禁用词的方法
     * @author 张逸辰
     * @Date 2020/7/15 13:37
     */
    void bannerMsgHandle(MessageEvent msg);
}
