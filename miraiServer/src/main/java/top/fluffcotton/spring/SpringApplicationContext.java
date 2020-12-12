package top.fluffcotton.spring;

import net.mamoe.mirai.event.Events;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.fluffcotton.listener.MsgListener;

/**
 * @Description TODO
 * @Author zyc
 * @Date 2020/12/10 18:59
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
        Events.registerEvents(new MsgListener());
    }
}
