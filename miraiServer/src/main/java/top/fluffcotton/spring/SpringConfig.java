package top.fluffcotton.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.TimeJob;

/**
 * @Description 拓展spring
 * @Author zyc
 * @Date 2020/12/6 22:48
 */
@Configuration
public class SpringConfig {
    @Bean
    public BotService getBotService(){
        return BotService.getBotService();
    }

    @Bean
    public TimeJob getTimeJob(){return TimeJob.getTimeJob();}
}
