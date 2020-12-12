package top.fluffcotton.spring;

import top.fluffcotton.config.Config;

/**
 * @Description 配置配置文件位置
 * @Author zyc
 * @Date 2020/12/6 23:04
 */
public class ConfigBean {

    private String configProperties;

    public void setConfigProperties(String configProperties) {
        this.configProperties = configProperties;
        Config.getConfig(configProperties);
    }
}
