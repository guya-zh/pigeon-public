package cn.guyasc.pigeon.core.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 对象判断工具类
 *
 * @author guya
 * @since 2023/11/23 10:32
 */
public class ObjectUtil {

    /**
     * 判断对象是否为空
     *
     * @param object 对象
     * @return true 空，false 不为空
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }
        return false;
    }

    /**
     * 隐藏字符
     *
     * @param content 字符串
     * @param start   开始位置 包含 从0开始
     * @param end     结束位置 包含
     * @return 处理结果
     */
    public static String hide(String content, int start, int end) {
        AssertUtil.isTrue(end > start, "参数异常");
        start = Math.max(start, 0);
        end = Math.min(end, content.length() - 1);
        StringBuilder temp = new StringBuilder();
        for (int i = start; i <= end; i++) {
            temp.append("*");
        }
        return content.substring(0, start) + temp + content.substring(end + 1);
    }
}
