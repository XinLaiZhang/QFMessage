package top.fluffcotton.listen.handle;

import net.mamoe.mirai.message.MessageEvent;

/**
 * @Description TODO
 * @Author zyc
 * @Date 2020/12/9 17:25
 */
public interface MsgHandle {
    /**
     * 执行操作，建议最顶层子接口重写execute方法以保证参数的实际意义 并 向外提供 默认 execute
     * @Title execute
     * @Description 执行的操作
     * @param msg 消息
     * @param args 处理时需要的参数
     * @return 指令执行情况
     * @author 张逸辰
     * @Date 2020/7/16 14:40
     */
    boolean execute(MessageEvent msg, Object... args);
    /**
     * 获取命令名称
     * @Title getName
     * @Description 获取命令名称
     * @return 命令名称
     * @author 张逸辰
     * @Date 2020/7/16 15:06
     */
    String getName();
    /**
     * 获取id，要求唯一
     * @Title getID
     * @Description 获取处理器id
     * @return 处理器id
     * @author 张逸辰
     * @Date 2020/7/30 10:47
     */
    String getID();

    /**
     * 匹配事件消息与处理器，判断是否处理
     *
     * @param msg       消息
     * @return true 处理，false 不处理
     * @Title matcher
     * @Description 匹配事件消息与处理器
     * @author 张逸辰
     * @Date 2020/7/18 16:29
     */
    boolean matcher(MessageEvent msg);

    /**
     * 获取处理器，处理的消息类型(私聊，讨论组，群)
     * @Title getMsgTypes
     * @Description 获取处理器，处理的消息类型
     * @return 处理消息类型数组
     * @author 张逸辰
     * @Date 2020/7/21 21:47
     */
    Class<? extends MessageEvent>[] getMsgTypes();
}
