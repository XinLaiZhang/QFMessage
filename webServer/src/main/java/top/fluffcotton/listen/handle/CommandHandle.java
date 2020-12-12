package top.fluffcotton.listen.handle;



import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.MessageEvent;

import java.util.Map;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: CommandHandle
 * @Description: 口令操作接口
 * @date 2020.07.16 14:36
 */
public interface CommandHandle {

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
     * 获取命令名称
     * @Title getName
     * @Description 获取命令名称
     * @return 命令名称
     * @author 张逸辰
     * @Date 2020/7/16 15:06
     */
    String getName();

    /**
     * 口令执行操作
     *
     * @param msg     消息
     * @param content 参数
     * @return 指令执行情况 小于等于0认为失败
     * @Title execute
     * @Description 处理口令执行的操作
     * @author 张逸辰
     * @Date 2020/7/16 14:40
     */
    long execute(MessageEvent msg, Map<String, String> content);

    /**
     * 匹配命令，返回一个匹配成功的Matcher，在此处理群匹配等
     *
     * @param msg       消息
     * @return 匹配成功返回参数
     * @Title match
     * @Description 匹配命令
     * @author 张逸辰
     * @Date 2020/7/16 14:53
     */
    Map<String, String> match(MessageEvent msg);

    /**
     * 验证是否有权限执行命令
     *
     * @param msg       消息
     * @param commander 命令者
     * @param content   参数
     * @return 成功//失败
     * @Title isHasPower
     * @Description 权限验证
     * @author 张逸辰
     * @Date 2020/7/16 15:03
     */
    boolean isHasPower(MessageEvent msg, Member commander, Map<String, String> content);

    /**
     * 获取执行成功回复信息 为空即为不回复
     *
     * @param code 成功编号 小于等于0认为失败
     * @return 成功回复信息
     * @Title getSuccessMsg
     * @Description 获取执行成功回复信息
     * @author 张逸辰
     * @Date 2020/7/16 16:43
     */
    String getSuccessMsg(long code);

    /**
     * 获取执行失败的回复信息 为空即为不回复
     *
     * @param code 失败编号，小于等于0为失败
     * @param msg  失败原因
     * @return 失败回复信息
     * @Title getFailMsg
     * @Description 获取执行失败回复信息
     * @author 张逸辰
     * @Date 2020/7/16 16:43
     */
    String getFailMsg(long code, String msg);

    /**
     * 匹配方法，在口令中不被使用，用于其他事件处理器中，一般由 抽象Impl实现 直接返回true
     *
     * @param msg       消息
     * @return 匹配是否成功
     * @Title matcher
     * @Description 匹配方法，在口令中不被使用
     * @author 张逸辰
     * @Date 2020/7/19 16:10
     */
    default boolean matcher(MessageEvent msg) {
        return true;
    }

    /**
     * 帮助信息文档，将在提示中显示，呈现形式如 name ： help;
     *
     * @return 帮助文档，简介指令如何使用
     * @Title getHelp
     * @Description 帮助文档
     * @author 张逸辰
     * @Date 2020/7/19 17:12
     */
    String getHelp();

}
