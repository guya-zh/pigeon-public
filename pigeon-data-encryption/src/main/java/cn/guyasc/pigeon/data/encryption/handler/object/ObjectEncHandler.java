package cn.guyasc.pigeon.data.encryption.handler.object;

import cn.guyasc.pigeon.data.encryption.annotation.DataEnc;

/**
 * 对象加密接口
 *
 * @author guya
 * @since 2024/1/2 11:46
 */
public interface ObjectEncHandler {

    /**
     * 加密
     *
     * @param data 明文字符串
     * @return 加密结果
     */
    String encrypt(String data);

    /**
     * 解密
     *
     * @param data 密文字符串
     * @return 解密结果
     */
    String decrypt(String data);

    /**
     * 对象加密加密结果直接替原对象属性，支持深度解密
     * 加密字段取{@link DataEnc}标记字段和keys合集
     *
     * @param object 泛型对象 Collection，Map,Array,其它非基本数据类型对象
     * @param keys   字段名（字段名相同则会自动处理,包含嵌套对象）
     * @return 对象本身
     */
    <T> T encrypt(T object, String... keys);

    /**
     * 对象解密，支持深度解密
     * 解密字段取{@link DataEnc}标记字段和keys合集
     * 修改原对象的值
     *
     * @param result 支持Collection，Map,Array,其它非基本数据类型对象
     * @param keys   字段名（字段名相同则会自动处理,包含嵌套对象）
     * @return 对象本身
     */
    <T> T decrypt(T result, String... keys);

    /**
     * 拷贝对象后加密，不修改源对象的值，支持深度加密
     * 加密字段取{@link DataEnc}标记字段和keys合集
     *
     * @param object 泛型对象 Collection，Map,Array,其它非基本数据类型对象
     * @param keys   字段名（字段名相同则会自动处理,包含嵌套对象）
     * @return 加密结果
     */
    <T> T encryptByClone(T object, String... keys);

    /**
     * 拷贝对象后解密，不修改源对象的值，支持深度解密密
     * 加密字段取{@link DataEnc}标记字段和keys合集
     *
     * @param object 泛型对象 Collection，Map,Array,其它非基本数据类型对象
     * @param keys   字段名（字段名相同则会自动处理,包含嵌套对象）
     * @return 加密结果
     */
    <T> T decryptByClone(T object, String... keys);

}
