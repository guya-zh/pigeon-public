package cn.guyasc.pigeon.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.time.temporal.TemporalAccessor;
import java.util.*;

/**
 * 对象判断工具类
 *
 * @author guya
 * @since 2023/11/23 10:32
 */
public class ObjectUtil {

    /**
     * 基本数据类型数组名称集合
     */
    private static final Set<String> BASIC_TYPE_ARRAY_CLASS_NAME = new HashSet<>();
    /**
     * 基本数据类型集合
     */
    private static final Set<Class<?>> BASIC_TYPE_CLASS_NAME = new HashSet<>();
    /**
     * 支持的继承层级
     */
    private static final int PARENT_LEVEL = 20;

    static {
        /*初始化*/
        BASIC_TYPE_ARRAY_CLASS_NAME.add("int[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("float[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("byte[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("short[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("long[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("double[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("char[]");
        BASIC_TYPE_ARRAY_CLASS_NAME.add("boolean[]");
        BASIC_TYPE_CLASS_NAME.add(Boolean.class);
        BASIC_TYPE_CLASS_NAME.add(Byte.class);
        BASIC_TYPE_CLASS_NAME.add(Character.class);
        BASIC_TYPE_CLASS_NAME.add(Double.class);
        BASIC_TYPE_CLASS_NAME.add(Float.class);
        BASIC_TYPE_CLASS_NAME.add(Integer.class);
        BASIC_TYPE_CLASS_NAME.add(Long.class);
        BASIC_TYPE_CLASS_NAME.add(Short.class);
    }

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

    /**
     * 获取对应字段的注解信息
     * 注解对象 不存在返回null
     *
     * @param object   对象
     * @param key      字段名
     * @param tagClazz 注解class
     */
    public static <T extends Annotation> T getFieldAnnotation(Object object, String key, Class<T> tagClazz) {
        Field field = getField(object, key);
        if (field != null) {
            return field.getAnnotation(tagClazz);
        }
        return null;
    }

    /**
     * 获取对应的字段 字段 不存在返回null
     *
     * @param object 对象
     * @param key    字段名
     */
    public static Field getField(Object object, String key) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        /*循环遍历字段，至多支持10层*/
        for (int i = 0; i < PARENT_LEVEL; i++) {
            if (clazz == null || clazz.isInterface()) {
                return null;
            }
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals(key)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 获取对象字段，包含private和父类字段，
     * 最多支持{@link ObjectUtil#PARENT_LEVEL}层继承
     */
    public static List<Field> getFields(Object object) {
        if (object == null) {
            return new ArrayList<>();
        }
        return getFields(object.getClass());
    }

    /**
     * 获取对象字段，包含private和父类字段，
     * 最多支持 {@link ObjectUtil#PARENT_LEVEL}层继承
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        if (clazz == null) {
            return fieldList;
        }
        /*控制对象继承层数*/
        for (int i = 0; i < PARENT_LEVEL; i++) {
            if (clazz == null || clazz.isInterface()) {
                return fieldList;
            }
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 判断对象是否是基本数据类型数组,不包含封装类型
     *
     * @param object 待判断对象
     * @return {@code false} 非基本数据类型数组或者null {@code true} 基本数据类型数组
     */
    public static boolean isBasicArray(Object object) {
        if (object == null) {
            return false;
        }
        Class<?> clazz = object.getClass();
        return clazz.isArray() && BASIC_TYPE_ARRAY_CLASS_NAME.contains(clazz.getSimpleName());
    }

    public static boolean isSimpleValueType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                BASIC_TYPE_CLASS_NAME.contains(clazz) ||
                clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                clazz.equals(URI.class) ||
                clazz.equals(URL.class) ||
                clazz.equals(Locale.class) ||
                clazz.equals(Class.class) ||
                TemporalAccessor.class.isAssignableFrom(clazz);
    }
}
