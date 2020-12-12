package top.fluffcotton.listener;

import net.mamoe.mirai.event.events.BotEvent;
import top.fluffcotton.config.ListenerConfig;
import top.fluffcotton.handle.Handle;
import top.fluffcotton.pojo.MatchResult;
import top.fluffcotton.spring.SpringApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @Description 监听器
 * @Author zyc
 * @Date 2020/11/23 21:03
 */
public class ListenerImpl implements Listener {

    /**
     * 消息类型
     */
    protected final Class<? extends BotEvent> listenerClass;
    /**
     * 统计执行次数
     */
    protected int count;
    /**
     * 执行器
     */
    protected PriorityBlockingQueue<Handle> handles;

    public ListenerImpl(Class<? extends BotEvent> listenerClass, int count) {
        this.listenerClass = listenerClass;
        this.count = count;
        init();
    }

    public ListenerImpl(Class<? extends BotEvent> listenerClass) {
        this.listenerClass = listenerClass;
        count = 0;
        init();
    }

    /**
     * 初始化handle队列
     */
    protected void init() {
        ListenerConfig listenerConfig = ListenerConfig.getListenerConfig();
        Map<Class<? extends Handle>, Integer> scannerClass = listenerConfig.getScannerClass(listenerClass);
        //初始化队列
        handles = new PriorityBlockingQueue<>(scannerClass.size() == 0 ? 1 : scannerClass.size(), (o1, o2) -> Integer.compare(o2.getCount(),o1.getCount()));
        for (Class<? extends Handle> aClass : scannerClass.keySet()) {
            try {
                Handle handle;
                try{
                    handle = SpringApplicationContext.getApplicationContext().getBean(aClass);
                }catch (Exception e){
                    handle = aClass.newInstance();
                }
                handle.setCount(scannerClass.get(aClass));
                handles.add(handle);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean execute(BotEvent event) {
        if (listenerClass.isInstance(event)) {
            Handle mHandle = null;
            for (Handle handle : handles) {
                if (handle.match(event).equals(MatchResult.SUCCESS)) {
                    //TODO 下个版本改成并发结构
                    mHandle = handle;
                    break;
                }
            }
            if(mHandle != null){
                //删除
                handles.remove(mHandle);
                mHandle.execute(event);
                mHandle.executeCount();
                //更新
                handles.add(mHandle);
                //更新执行次数
                count++;
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Class<? extends BotEvent> getListenerClass() {
        return this.listenerClass;
    }

    @Override
    public Map<Class<? extends Handle>, Integer> getHandleCount() {
        Map<Class<? extends Handle>, Integer> map = new HashMap<>();
        for (Handle handle : handles) {
            map.put(handle.getClass(),handle.getCount());
        }
        return map;
    }
}
