package top.fluffcotton.config;

import top.fluffcotton.service.ListenerService;
import top.fluffcotton.service.TimeJob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;

/**
 * @Description 负责读取配置文件供应用使用
 * @Author zyc
 * @Date 2020/12/2 13:50
 */
public class Config {
    /**
     * 扫描时间
     */
    public static final long SCANNER_TIME =300000L;
    /**
     * 保存时间间隔
     */
    public static final long SAVE_TIME = 400000L;
    /**
     * 配置文件路径
     */
    public static String CONFIG_PROPERTIES = "config/listenerConfig.properties";
    /**
     * 配置文件
     */
    public static File RESOURCE = null;
    /**
     * 单例
     */
    private static Config config;
    /**
     * 配置
     */
    private Properties properties;

    private Config(){
        RESOURCE = new File(Objects.requireNonNull(ListenerService.class.getClassLoader().getResource("")).getPath() + CONFIG_PROPERTIES);
        //定时扫描
        ScheduledFuture<?> getProperties = TimeJob.getTimeJob().start(this::init, System.currentTimeMillis(), SCANNER_TIME);
        //保存
        ScheduledFuture<?> saveProperties = TimeJob.getTimeJob().start(this::propertiesSave, System.currentTimeMillis() + SAVE_TIME, SAVE_TIME);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            getProperties.cancel(false);
            saveProperties.cancel(false);
            this.propertiesSave();
        }));
    }

    public static Config getConfig(){
        if(config == null){
            synchronized (Config.class){
                if(config == null){
                    config = new Config();
                }
            }
        }
        return config;
    }

    public static Config getConfig(String configProperies){
        CONFIG_PROPERTIES = configProperies;
        if(config == null){
            synchronized (Config.class){
                if(config == null){
                    config = new Config();
                }
            }
        }
        return config;
    }

    private void init() {
        readProperties();
    }

    /**
     * 从文件获取配置信息
     */
    private synchronized void readProperties() {
        properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(RESOURCE);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            propertiesSave();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    fileInputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存配置文件输入到文件
     */
    public synchronized void propertiesSave() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(RESOURCE);
            properties.store(fileOutputStream, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    fileOutputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

}
