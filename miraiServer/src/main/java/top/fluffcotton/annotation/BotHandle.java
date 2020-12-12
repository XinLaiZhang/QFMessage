package top.fluffcotton.annotation;


import net.mamoe.mirai.event.events.BotEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记该注解证明该类作为处理事件的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BotHandle {
    /**
     * 监听类型，是BotEvent类型
     */
    Class<? extends BotEvent>[] value();
}
