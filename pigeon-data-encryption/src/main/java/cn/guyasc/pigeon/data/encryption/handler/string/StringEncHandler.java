package cn.guyasc.pigeon.data.encryption.handler.string;

import java.util.Set;

/**
 * 基础加密类
 *
 * @author guya
 * @since 2024/1/16 14:41
 */
public interface StringEncHandler {
    /**
     * 支持加解密的前缀集合
     */
    Set<String> getPrefix();

    /**
     * 加密
     */
    String encrypt(String content);


    /**
     * 解密
     */
    String decrypt(String content);
}
