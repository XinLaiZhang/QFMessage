package top.fluffcotton.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description 实现机器人配置文件的获取
 * @Author zyc
 * @Date 2020/11/22 14:47
 */
public class BotConfig {




    private static BotConfig botConfig;
    /**
     * 读取配置文件类
     */
    private final Config config;
    /**
     * 存机器人qq号密码
     */
    private Map<Long,String> qqBot;

    private BotConfig() {
        config = Config.getConfig();
        init();
    }
    public static BotConfig getBotConfig() {
        if (botConfig == null) {
            synchronized (BotConfig.class){
                if(botConfig == null){
                    botConfig = new BotConfig();
                }
            }
        }
        return botConfig;
    }

    private void init() {
        Properties properties = config.getProperties();
        String bot = (String) properties.getOrDefault("bot", "");
        String[] split = bot.split("[,，]");
        qqBot = new HashMap<>(Math.max(split.length,1));
        for (String s : split) {
            int index = s.indexOf(":");
            if(index < 5){
                continue;
            }
            qqBot.put(Long.parseLong(s.substring(0,index)),s.substring(index+1));
        }
    }

    public Map<Long, String> getQqBot() {
        return qqBot;
    }
}
