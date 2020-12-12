package top.fluffcotton.listener;

import net.mamoe.mirai.event.events.BotEvent;
import top.fluffcotton.handle.Handle;

import java.util.Map;

/**
 * @Description 监听器 所以监听器要求实现该接口
 * 还需要在构造参数上添加count 以保证count能够顺利注入
 * @Author zyc
 * @Date 2020/11/23 20:10
 */
public interface Listener {

    /**
     * 调用执行，内部自动判断是否接受，同时完成执行次数的累加
     * @param event 事件
     * @return true接受，且不向下传递
     * false 继续向下传递
     */
    boolean execute(BotEvent event) throws Exception;

    /**
     * 获取Handle执行次数
     * @return 执行次数
     */
    int getCount();

    /**
     * 获取匹配的类型
     * @return 匹配的类型
     */
    Class<? extends BotEvent> getListenerClass();

    /**
     * 获取执行器与其执行次数，执行器的class与次数
     */
    Map<Class<? extends Handle>,Integer> getHandleCount();
}
