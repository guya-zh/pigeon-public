package cn.guyasc.pigeon.core.util;

/**
 * 断言工具
 *
 * @author guya
 * @since 2023/11/23 10:44
 */
public class AssertUtil {
    /**
     * 断言结果一定为真，为假时抛出异常
     *
     * @param flag 判断标志
     * @param msg  提示消息
     */
    public static void isTrue(boolean flag, String msg) {
        if (!flag) {
            throw new RuntimeException(msg);
        }
    }
}
