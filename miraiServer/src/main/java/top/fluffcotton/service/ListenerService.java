package top.fluffcotton.service;

import net.mamoe.mirai.event.events.BotEvent;
import top.fluffcotton.config.Config;
import top.fluffcotton.config.ListenerConfig;
import top.fluffcotton.listener.Listener;
import top.fluffcotton.listener.ListenerImpl;

import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 采用优先队列结构，遍历后根据执行次数排序提高命中率
 * 通过配置文件直接获取并且自动生成不同类型的监听器
 * @Description 用于配置监听器服务
 * @Author zyc
 * @Date 2020/11/24 23:16
 */
public class ListenerService {

    /**
     * 单例
     */
    private static ListenerService listenerService;

    /**
     * 监听器优先队列，提高访问效率
     */
    private PriorityBlockingQueue<Listener> listeners;

    /**
     * 配置
     */
    private ListenerConfig listenerConfig;

    protected ListenerService() {
        init();
        // 定时从配置文件获取执行器
        TimeJob.getTimeJob().start(this::addListener,System.currentTimeMillis() + Config.SCANNER_TIME, Config.SCANNER_TIME);
    }

    /**
     * 工厂初始化
     *
     * @return 监听服务
     */
    public static ListenerService getListenerService() {
        if (listenerService == null) {
            synchronized (ListenerService.class) {
                listenerService = new ListenerService();
            }
        }
        return listenerService;
    }

    /**
     * 根据配置文件初始化监听器
     */
    private void init() {
        listenerConfig = ListenerConfig.getListenerConfig();
        Map<Class<? extends BotEvent>, Integer> listener = listenerConfig.getListener();
        //创建监听器
        listeners = new PriorityBlockingQueue<>(listener.size(), (o1, o2) -> Integer.compare(o2.getCount(),o1.getCount()));
        for (Class<? extends BotEvent> aClass : listener.keySet()) {
            listeners.add(new ListenerImpl(aClass,listener.get(aClass)));
        }
    }

    /**
     * 添加监听器，通过扫描后的
     */
    private void addListener(){
        Map<Class<? extends BotEvent>, Integer> newListener = listenerConfig.getListener();
        for (Listener listener : listeners) {
            newListener.remove(listener.getListenerClass());
        }
        for (Class<? extends BotEvent> aClass : newListener.keySet()) {
            listeners.add(new ListenerImpl(aClass,newListener.get(aClass)));
        }
    }

    /**
     * 执行事件
     * @param event 事件
     */
    public void execute(BotEvent event) throws Exception{
        Listener l = null;
        for (Listener listener : listeners) {
            if(listener.execute(event)){
                l = listener;
                break;
            }
        }
        //处理更新监听器顺序
        if(l != null){
            listeners.remove(l);
            listeners.add(l);
        }
    }

    /**
     * 保存监听器及其执行次数到配置文件
     */
    public synchronized void saveListener() {
        listenerConfig.save();
    }

    public PriorityBlockingQueue<Listener> getListeners() {
        return listeners;
    }

}
