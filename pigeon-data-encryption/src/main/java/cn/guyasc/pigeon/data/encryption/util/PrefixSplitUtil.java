package cn.guyasc.pigeon.data.encryption.util;

import cn.guyasc.pigeon.core.util.AssertUtil;

/**
 * 加密数据前缀分离器
 *
 * @author guya
 * @since 2024/1/17 16:06
 */
public class PrefixSplitUtil {
    private static volatile PrefixSplitHandler handler;

    public static synchronized void init(PrefixSplitUtil.PrefixSplitHandler handler) {
        AssertUtil.isTrue(PrefixSplitUtil.handler == null, "类加载器已经初始化不能重复初始化");
        PrefixSplitUtil.handler = handler;
    }

    public static PrefixSplitHandler getHandler() {
        if (handler == null) {
            synchronized (PrefixSplitUtil.class) {
                if (handler == null) {
                    handler = new DefaultHandler();
                }
            }
        }
        return handler;
    }

    /**
     * 加载bean
     */
    public static String getPrefix(String content) {
        return getHandler().getPrefix(content);
    }


    public interface PrefixSplitHandler {
        /**
         * 获取前缀
         */
        String getPrefix(String content);

    }

    public static class DefaultHandler implements PrefixSplitHandler {
        @Override
        public String getPrefix(String content) {
            int index = content.indexOf("-");
            if (index > 0) {
                return content.substring(0, index);
            }
            return "";
        }
    }
}
