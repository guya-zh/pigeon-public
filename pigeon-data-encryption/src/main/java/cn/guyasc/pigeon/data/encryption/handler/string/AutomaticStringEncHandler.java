package cn.guyasc.pigeon.data.encryption.handler.string;

import cn.guyasc.pigeon.core.util.AssertUtil;
import cn.guyasc.pigeon.core.util.BeanLoadUtil;
import cn.guyasc.pigeon.data.encryption.util.PrefixSplitUtil;

import java.util.*;

/**
 * @author guya
 * @since 2024/1/17 15:01
 */
public class AutomaticStringEncHandler implements StringEncHandler {
    private final Map<String, BaseStringEncHandler> handlerMap = new HashMap<>();
    private volatile BaseStringEncHandler defaultHandler;
    private final Set<String> prefix = new HashSet<>();
    private volatile boolean init = false;

    public AutomaticStringEncHandler() {
        if (!init) {
            synchronized (AutomaticStringEncHandler.class) {
                if (!init) {
                    TreeSet<BaseStringEncHandler> handlers = BeanLoadUtil.load(BaseStringEncHandler.class);
                    for (BaseStringEncHandler handler : handlers) {
                        for (String handlerPrefix : handler.getPrefix()) {
                            AssertUtil.isTrue(!handlerMap.containsKey(handlerPrefix), "不同的加密处理器前缀不能相同，prefix:" + handlerPrefix);
                            handlerMap.put(handlerPrefix, handler);
                            prefix.add(handlerPrefix);
                        }
                        if (handler.isDefault()) {
                            AssertUtil.isTrue(defaultHandler == null, "默认加密处理器不能存在多个");
                            defaultHandler = handler;
                        }
                    }
                    init = true;
                }
            }
        }
    }

    @Override
    public Set<String> getPrefix() {
        return prefix;
    }

    @Override
    public String encrypt(String content) {
        if (defaultHandler == null) {
            return content;
        }
        return defaultHandler.encrypt(content);
    }

    @Override
    public String decrypt(String content) {
        BaseStringEncHandler handler = handlerMap.get(PrefixSplitUtil.getPrefix(content));
        if (handler == null) {
            return content;
        }
        return handler.decrypt(content);
    }
}
