package cn.guyasc.pigeon.core.util;

import java.util.ServiceLoader;
import java.util.TreeSet;

/**
 * bean加载工具
 *
 * @author guya
 * @since 2024/1/17 15:06
 */
public class BeanLoadUtil {
    private static volatile BeanLoadHandler handler;

    public static synchronized void init(BeanLoadHandler handler) {
        AssertUtil.isTrue(BeanLoadUtil.handler == null, "类加载器已经初始化不能重复初始化");
        BeanLoadUtil.handler = handler;
    }

    public static BeanLoadHandler getHandler() {
        if (handler == null) {
            synchronized (BeanLoadUtil.class) {
                if (handler == null) {
                    handler = new JavaSpiLoadHandler();
                }
            }
        }
        return handler;
    }

    /**
     * 加载bean
     */
    public static <T> TreeSet<T> load(Class<T> clazz) {
        return getHandler().load(clazz);
    }

    /**
     * 使用name加载bean
     */
    public static <T> T load(String name) {
        return getHandler().load(name);
    }

    public interface BeanLoadHandler {
        /**
         * 根据类型加载
         */
        <T> TreeSet<T> load(Class<T> clazz);

        /**
         * 根据名称加载
         */
        <T> T load(String name);
    }

    public static class JavaSpiLoadHandler implements BeanLoadHandler {
        @Override
        public <T> TreeSet<T> load(Class<T> clazz) {
            ServiceLoader<T> load = ServiceLoader.load(clazz);
            ServiceLoader.load(clazz);
            TreeSet<T> treeSet = new TreeSet<>();
            for (T t : load) {
                treeSet.add(t);
            }
            return treeSet;
        }

        @Override
        public <T> T load(String name) {
            throw new RuntimeException("java spi 加载器不支持通过名称加载对象");
        }
    }
}
