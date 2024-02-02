package cn.guyasc.pigeon.data.encryption.annotation;

import cn.guyasc.pigeon.data.encryption.handler.string.AutomaticStringEncHandler;
import cn.guyasc.pigeon.data.encryption.handler.string.StringEncHandler;

import java.lang.annotation.*;

/**
 * 加密注解标记在字段上
 *
 * @author guya
 * @since 2024/1/2 14:09
 */

@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataEnc {

    /**
     * 指定处理类，默认自动根据前缀识别
     */
    Class<? extends StringEncHandler> handler() default AutomaticStringEncHandler.class;
}
