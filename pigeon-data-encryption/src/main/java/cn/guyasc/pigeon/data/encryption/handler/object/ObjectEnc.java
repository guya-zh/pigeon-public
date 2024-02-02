package cn.guyasc.pigeon.data.encryption.handler.object;

import cn.guyasc.pigeon.core.util.AssertUtil;
import cn.guyasc.pigeon.core.util.BeanLoadUtil;
import cn.guyasc.pigeon.core.util.ObjectUtil;
import cn.guyasc.pigeon.data.encryption.annotation.DataEnc;
import cn.guyasc.pigeon.data.encryption.handler.string.StringEncHandler;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 对象加密实例，线程不安全，单个对象只能在单线程使用
 *
 * @author guya
 * @since 2024/1/18 11:45
 */
public class ObjectEnc {

    /**
     * keys缓存
     */
    private final Set<String> keySet = new HashSet<>();

    private volatile Map<String, StringEncHandler> handlerMap;

    private StringEncHandler getHandlerByClass(Class<? extends StringEncHandler> clazz) {
        if (handlerMap == null) {
            synchronized (ObjectEnc.class) {
                if (handlerMap == null) {
                    TreeSet<StringEncHandler> handlers = BeanLoadUtil.load(StringEncHandler.class);
                    AssertUtil.isTrue(!ObjectUtil.isEmpty(handlers), "处理类不存在");
                    handlerMap = new ConcurrentHashMap<>();
                    for (StringEncHandler handler : handlers) {
                        handlerMap.put(handler.getClass().getName(), handler);
                    }
                }
            }
        }
        return handlerMap.get(clazz.getName());
    }

    /**
     * 对象处理器
     *
     * @param object       对象
     * @param strHandler   String处理器
     * @param key          处理对象对应的key值
     * @param handlerClass 默认的处理工具
     * @param keys         处理字段
     * @param isMark       如果为true string数组和string集合都可以脱敏
     */
    public void execute(Object object,
                        Function<EncInfo, String> strHandler,
                        String key,
                        Class<? extends StringEncHandler> handlerClass,
                        boolean isMark,
                        String... keys) throws IllegalAccessException {
        if (object == null || strHandler == null) {
            return;
        }
        if (!ObjectUtil.isEmpty(keys) && keySet.isEmpty()) {
            keySet.addAll(Arrays.asList(keys));
        }
        if (object instanceof Map) {
            map((Map) object, strHandler, keys);
        } else if (object instanceof Collection) {
            collection((Collection) object, strHandler, key, handlerClass, isMark, keys);
        } else if (object.getClass().isArray() && !ObjectUtil.isBasicArray(object)) {
            array((Object[]) object, strHandler, key, handlerClass, isMark, keys);
        } else if (!ObjectUtil.isSimpleValueType(object.getClass())) {
            object(object, strHandler, keys);
        }
    }

    /**
     * 对象处理器
     *
     * @param object        对象
     * @param keyEncHandler 外部指定key的处理类集合
     * @param encFunction   字符串加密
     * @param keys          处理字段
     */
    @SneakyThrows
    public void execute(Object object,
                        Map<String, StringEncHandler> keyEncHandler,
                        BiFunction<StringEncHandler, String, String> encFunction,
                        String... keys) {
        Function<EncInfo, String> function = (encInfo) -> {
            StringEncHandler handler = (keyEncHandler == null || encInfo.getKey() == null) ? null : keyEncHandler.get(encInfo.getKey());
            if (handler == null && encInfo.getStringEncHandler() != null) {
                handler = getHandlerByClass(encInfo.getStringEncHandler());
            }
            return encFunction.apply(handler, encInfo.getValue());
        };
        execute(object, function, null, null, false, keys);
    }

    /**
     * 对象处理器
     *
     * @param object      对象
     * @param encFunction 字符串加密
     * @param keys        处理字段
     */
    @SneakyThrows
    public void execute(Object object,
                        BiFunction<StringEncHandler, String, String> encFunction,
                        String... keys) {
        execute(object, null, encFunction, keys);
    }

    /**
     * map对象处理器
     *
     * @param map        对象
     * @param strHandler String处理器
     * @param keys       处理字段
     */
    protected void map(Map<Object, Object> map, Function<EncInfo, String> strHandler, String... keys) throws IllegalAccessException {
        Set<Object> objects = map.keySet();
        for (Object objectKey : objects) {
            Object value = map.get(objectKey);
            if (value == null) {
                continue;
            }
            boolean isMarked = objectKey instanceof String && keySet.contains(objectKey);
            String key = null;
            if (objectKey != null) {
                key = objectKey.toString();
            }
            if (value instanceof String) {
                if (isMarked) {
                    map.put(objectKey, strHandler.apply(new EncInfo(key, (String) value, null)));
                }
            } else {
                execute(value, strHandler, key, null, isMarked, keys);
            }
        }
    }

    /**
     * Collection对象处理器
     *
     * @param list         对象
     * @param strHandler   String处理器
     * @param key          处理对象对应的key值
     * @param handlerClass 默认的处理工具
     * @param keys         处理字段
     * @param isMark       是否被标记（如果被标记Collection<String> 类型的数据也会处理 ）
     */
    protected void collection(Collection<Object> list,
                              Function<EncInfo, String> strHandler,
                              String key,
                              Class<? extends StringEncHandler> handlerClass,
                              boolean isMark,
                              String... keys) throws IllegalAccessException {
        List<Object> temp = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof String) {
                if (isMark) {
                    temp.add(strHandler.apply(new EncInfo(key, (String) object, handlerClass)));
                }
            } else {
                if (isMark) {
                    temp.add(object);
                }
                execute(object, strHandler, null, null, false, keys);
            }
        }
        if (!temp.isEmpty()) {
            list.clear();
            list.addAll(temp);
        }
    }

    /**
     * array对象处理器
     *
     * @param objects      对象
     * @param strHandler   String处理器
     * @param keys         处理字段
     * @param key          处理对象对应的key值
     * @param handlerClass 默认的处理工具
     * @param isMark       是否被标记（如果被标记String数组数据也会处理 ）
     */
    protected void array(Object[] objects,
                         Function<EncInfo, String> strHandler,
                         String key, Class<? extends StringEncHandler> handlerClass,
                         boolean isMark,
                         String... keys) throws IllegalAccessException {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof String) {
                if (isMark) {
                    objects[i] = strHandler.apply(new EncInfo(key, (String) objects[i], handlerClass));
                }
            } else {
                execute(objects[i], strHandler, null, null, false, keys);
            }
        }
    }

    /**
     * 普通对象处理器，不包含集合等
     *
     * @param object   对象
     * @param function String处理器
     * @param keys     处理字段
     */
    protected void object(Object object, Function<EncInfo, String> function, String... keys) throws IllegalAccessException {
        List<Field> declaredFields = ObjectUtil.getFields(object);
        if (ObjectUtil.isEmpty(declaredFields)) {
            return;
        }
        for (Field field : declaredFields) {
            field.setAccessible(true);
            /*静态 属性不处理,final 属性不处理*/
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(modifiers)) {
                continue;
            }
            Object value = field.get(object);
            if (value == null) {
                continue;
            }
            String fieldName = field.getName();
            DataEnc dataEnc = null;
            if (!(value instanceof String)) {
                boolean isMarked = keySet.contains(field.getName()) ||
                        !Objects.isNull(dataEnc = field.getAnnotation(DataEnc.class));
                execute(value, function, fieldName, getHandlerClass(dataEnc), isMarked, keys);
            } else if (!ObjectUtil.isEmpty(value)) {
                if (keySet.contains(field.getName()) ||
                        !Objects.isNull((dataEnc = field.getAnnotation(DataEnc.class)))) {
                    field.set(object, function.apply(new EncInfo(fieldName, (String) value, getHandlerClass(dataEnc))));
                }
            }
        }
    }

    private Class<? extends StringEncHandler> getHandlerClass(DataEnc dataEnc) {
        return dataEnc == null ? null : dataEnc.handler();
    }

}
