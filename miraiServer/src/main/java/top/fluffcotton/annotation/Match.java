package top.fluffcotton.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅用于检测该方法为匹配方法
 * 要求类名使用@BotHandle注解
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Match {
}
