package top.fluffcotton.config;

import net.mamoe.mirai.event.events.BotEvent;
import top.fluffcotton.annotation.AnnotationScanner;
import top.fluffcotton.annotation.BotHandle;
import top.fluffcotton.handle.Handle;
import top.fluffcotton.listener.Listener;
import top.fluffcotton.service.ListenerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 每五分钟执行一次扫描
 *
 * @Description 监听器配置类
 * @Author zyc
 * @Date 2020/11/28 18:46
 */
public class ListenerConfig {

    /**
     * 监听器
     */
    public static final String LISTENER = "listener";
    /**
     * 监听器执行次数
     */
    public static final String LISTENER_COUNT = "listenerCount";
    /**
     * 扫描的路径
     */
    public static final String CLASSPATH = "CLASSPATH";
    /**
     * 监听器配置类（单例）
     */
    private static ListenerConfig listenerConfig;
    /**
     * 读取配置文件类
     */
    private final Config config;
    /**
     * 监听器
     */
    private Map<Class<? extends BotEvent>, Integer> listener;
    /**
     * 扫描到的Class缓存
     */
    private Set<Class<?>> scannerClass;

    private ListenerConfig() {
        config = Config.getConfig();
        init();
    }

    public static ListenerConfig getListenerConfig() {
        if (listenerConfig == null) {
            synchronized (ListenerConfig.class){
                if(listenerConfig == null){
                    listenerConfig = new ListenerConfig();
                }
            }
        }
        return listenerConfig;
    }

    public synchronized void init() {
        // 获取配置文件内需要监听的listener 和 他们历史执行次数
        getListenerProperties(config.getProperties());
        //扫描监听器
        scannerClass(config.getProperties());
    }

    /**
     * 获取监听器配置
     */
    private void getListenerProperties(Properties properties) {
        String listener = (String) properties.getOrDefault(LISTENER, "");
        String listenerCount = (String) properties.getOrDefault(LISTENER_COUNT, "");
        String[] listenerClass = listener.split("[,，]");
        String[] count = listenerCount.split("[,，]");
        this.listener = new HashMap<>();
        for (int i = 0; i < listenerClass.length; i++) {
            try {
                this.listener.put((Class<? extends BotEvent>) Class.forName(listenerClass[i]), Integer.parseInt(count[i]));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 扫描Class
     */
    private void scannerClass(Properties properties) {
        String classPath = (String) properties.getOrDefault(CLASSPATH, "");
        if (!"".equals(classPath)) {
            try {
                //获取扫描到的类
                scannerClass = AnnotationScanner.getAnnotationClasses(classPath, BotHandle.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取实现指定监听器的class
     *
     * @param bClass 监听类型
     * @return map key为Handle类，value为执行次数
     */
    public synchronized Map<Class<? extends Handle>, Integer> getScannerClass(Class<? extends BotEvent> bClass) {
        Map<Class<? extends Handle>, Integer> map = new HashMap<>();
        //遍历寻找实现了handle接口同时符合监听的类
        for (Class<?> sClass : scannerClass) {
            BotHandle annotation = sClass.getAnnotation(BotHandle.class);
            //判断是否实现接口
            if (Handle.class.isAssignableFrom(sClass)) {
                //获取监听值
                Class<? extends BotEvent>[] value = annotation.value();
                for (Class<? extends BotEvent> aClass : value) {
                    //满足的
                    if (bClass.isAssignableFrom(aClass)) {
                        //从配置文件读取执行次数
                        map.put((Class<? extends Handle>) sClass, Integer.parseInt((String) config.getProperties().getOrDefault(aClass.getName(), "0")));
                        break;
                    }
                }
            }
        }
        return map;
    }

    /**
     * 保存配置
     */
    public synchronized void save() {
        listenerSave(config.getProperties());
    }

    private void listenerSave(Properties properties) {
        PriorityBlockingQueue<Listener> listeners = ListenerService.getListenerService().getListeners();
        //保存handle
        StringBuilder listenerStr = new StringBuilder();
        StringBuilder countStr = new StringBuilder();
        for (Listener listener1 : listeners) {
            Map<Class<? extends Handle>, Integer> handleCount = listener1.getHandleCount();
            Set<Class<? extends Handle>> classes = handleCount.keySet();
            for (Class<? extends Handle> aClass : classes) {
                properties.setProperty(aClass.getName(), String.valueOf(handleCount.get(aClass)));
            }
            listenerStr.append(listener1.getListenerClass().getName()).append(",");
            countStr.append(listener1.getCount()).append(",");
        }
        properties.put(LISTENER, listenerStr.substring(0, listenerStr.length() - 1));
        properties.put(LISTENER_COUNT, countStr.substring(0, countStr.length() - 1));
    }

    public Map<Class<? extends BotEvent>,Integer> getListener() {
        return listener;
    }

}
