package cn.guyasc.pigeon.data.encryption.handler.object;

import cn.guyasc.pigeon.core.util.BeanLoadUtil;
import cn.guyasc.pigeon.core.util.JacksonUtil;
import cn.guyasc.pigeon.data.encryption.handler.string.AutomaticStringEncHandler;
import cn.guyasc.pigeon.data.encryption.handler.string.BaseStringEncHandler;
import cn.guyasc.pigeon.data.encryption.handler.string.StringEncHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认加密实现
 *
 * @author guya
 * @since 2024/1/2 16:03
 */
public class DefaultObjectEncHandler implements ObjectEncHandler {

    protected final AutomaticStringEncHandler automaticStringEncHandler;

    protected final Map<String, StringEncHandler> handlerMap = new ConcurrentHashMap<>();

    public DefaultObjectEncHandler() {
        this.automaticStringEncHandler = BeanLoadUtil.load(AutomaticStringEncHandler.class).first();
        for (BaseStringEncHandler stringEncHandler : BeanLoadUtil.load(BaseStringEncHandler.class)) {
            handlerMap.put(stringEncHandler.getClass().getName(), stringEncHandler);
        }
    }

    @Override
    public String encrypt(String data) {
        return automaticStringEncHandler.encrypt(data);
    }

    @Override
    public String decrypt(String data) {
        return automaticStringEncHandler.decrypt(data);
    }

    private String encrypt(StringEncHandler stringEncHandler, String data) {
        if (stringEncHandler != null) {
            return stringEncHandler.encrypt(data);
        }
        return automaticStringEncHandler.encrypt(data);
    }

    private String decrypt(StringEncHandler stringEncHandler, String data) {
        if (stringEncHandler != null) {
            return stringEncHandler.decrypt(data);
        }
        return automaticStringEncHandler.decrypt(data);
    }

    @Override
    public <T> T encrypt(T object, String... keys) {
        new ObjectEnc().execute(object, this::encrypt, keys);
        return object;
    }

    @Override
    public <T> T decrypt(T result, String... keys) {
        new ObjectEnc().execute(result, this::decrypt, keys);
        return result;
    }

    @Override
    public <T> T encryptByClone(T object, String... keys) {
        String jsonStr = JacksonUtil.toJsonStr(object);
        return encrypt((T) JacksonUtil.toObject(jsonStr, object.getClass()), keys);
    }

    @Override
    public <T> T decryptByClone(T object, String... keys) {
        String jsonStr = JacksonUtil.toJsonStr(object);
        return decrypt((T) JacksonUtil.toObject(jsonStr, object.getClass()), keys);
    }

}
