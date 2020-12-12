package top.fluffcotton.handle;

import net.mamoe.mirai.event.events.BotEvent;
import top.fluffcotton.pojo.MatchResult;

/**
 * 用于处理各种监听器事件
 * @Description 用于处理各种监听器事件
 * @Author zyc
 * @Date 2020/11/21 23:12
 */
public interface Handle {

    /**
     * @param event 事件
     * @return 是否被匹配，并且
     */
    MatchResult match(BotEvent event);

    /**
     * 执行事件应有的操作
     * @param event 事件
     */
    void execute(BotEvent event);

    /**
     * 获取执行次数
     * @return 执行次数
     */
    int getCount();

    /**
     * 修改执行次数
     * @param count 新的执行次数
     */
    void setCount(int count);

    /**
     * 执行该方法，执行次数+1
     */
    default void executeCount(){
        setCount(getCount()+1);
    }
}