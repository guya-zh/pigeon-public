package cn.guyasc.pigeon.data.encryption.handler.string;

/**
 * 字符串加密基本的操作类
 *
 * @author guya
 * @since 2024/1/17 15:58
 */
public interface BaseStringEncHandler extends StringEncHandler {
    /**
     * 指定默认加密处理器
     * true 默认，false 非默认
     */
    boolean isDefault();
}
