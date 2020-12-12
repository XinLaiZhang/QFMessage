package top.fluffcotton.service;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.utils.BotConfiguration;
import top.fluffcotton.config.BotConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @Description 用于管理机器人
 * @Author zyc
 * @Date 2020/11/22 14:41
 */
public class BotService {

    public static final int FRIEND_MAX = 1500;
    private static BotService botService;
    /**
     * 机器人列表
     */
    private final List<Bot> botList;

    private BotService() {
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.loadDeviceInfoJson("{\"display\":[77,73,82,65,73,46,51,52,53,48,51,51,46,48,48,49],\"product\":[109,105,114,97,105],\"device\":[109,105,114,97,105],\"board\":[109,105,114,97,105],\"brand\":[109,97,109,111,101],\"model\":[109,105,114,97,105],\"bootloader\":[117,110,107,110,111,119,110],\"fingerprint\":[109,97,109,111,101,47,109,105,114,97,105,47,109,105,114,97,105,58,49,48,47,77,73,82,65,73,46,50,48,48,49,50,50,46,48,48,49,47,52,49,53,50,48,53,48,58,117,115,101,114,47,114,101,108,101,97,115,101,45,107,101,121,115],\"bootId\":[65,55,48,57,52,68,69,56,45,54,50,70,48,45,56,55,55,66,45,55,50,56,55,45,57,55,48,51,49,50,68,49,55,51,49,51],\"procVersion\":[76,105,110,117,120,32,118,101,114,115,105,111,110,32,51,46,48,46,51,49,45,116,83,79,55,110,53,100,87,32,40,97,110,100,114,111,105,100,45,98,117,105,108,100,64,120,120,120,46,120,120,120,46,120,120,120,46,120,120,120,46,99,111,109,41],\"baseBand\":[],\"version\":{},\"simInfo\":[84,45,77,111,98,105,108,101],\"osType\":[97,110,100,114,111,105,100],\"macAddress\":[48,50,58,48,48,58,48,48,58,48,48,58,48,48,58,48,48],\"wifiBSSID\":[48,50,58,48,48,58,48,48,58,48,48,58,48,48,58,48,48],\"wifiSSID\":[60,117,110,107,110,111,119,110,32,115,115,105,100,62],\"imsiMd5\":[-59,126,-111,-107,69,-110,1,-27,-71,12,102,-38,-96,99,-26,-4],\"imei\":\"705522638367383\",\"apn\":[119,105,102,105]}");
//        botConfiguration.fileBasedDeviceInfo();
        //获取配置文件
        BotConfig botConfig = BotConfig.getBotConfig();
        Map<Long, String> qqBot = botConfig.getQqBot();
        botList = new ArrayList<>(qqBot.size());
        //登录
        for (Long aLong : qqBot.keySet()) {
            try{
                Bot bot = BotFactoryJvm.newBot(aLong, qqBot.get(aLong), botConfiguration);
                bot.login();
                botList.add(bot);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static BotService getBotService(){
        if(botService == null){
            synchronized (BotService.class){
                if(botService == null) {
                    botService = new BotService();
                }
            }
        }
        return botService;
    }

    public List<Bot> getBotList() {
        return botList;
    }

    /**
     * 根据群号获取群，若不存在则返回null
     * @param groupId 群号
     * @return 群
     */
    public Group getGroup(long groupId){
        for (Bot bot : botList) {
            try {
                return bot.getGroup(groupId);
            }catch (NoSuchElementException e){
                //TODO 写日志
            }
        }
        return null;
    }

    /**
     * 根据qq获取好友
     * @param qq 根据qq获取好友
     * @return 好友
     */
    public Friend getFriend(long qq){
        for (Bot bot : botList) {
            try{
                return bot.getFriend(qq);
            }catch (NoSuchElementException e){
                //TODO 写日志
            }
        }
        return null;
    }

    public Bot defaultBot() {
        for (Bot bot : botList) {
            if(bot.getFriends().size() < FRIEND_MAX){
                return bot;
            }
        }
        return null;
    }

}
