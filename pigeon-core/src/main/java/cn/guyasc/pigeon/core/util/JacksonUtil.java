package cn.guyasc.pigeon.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;

/**
 * @author guya
 * @since 2024/1/19 17:50
 */
public class JacksonUtil {
    /**
     * 日期格式
     */
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 所有的日期格式都统一为以下的样式，即 yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        // 支持 LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转Json格式字符串
     */
    @SneakyThrows
    public static <T> String toJsonStr(T obj) {
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }
        return OBJECT_MAPPER.writeValueAsString(obj);
    }


    /**
     * 字符串转换为自定义对象
     */
    @SneakyThrows
    public static <T> T toObject(String jsonStr, Class<T> clazz) {
        if (ObjectUtil.isEmpty(jsonStr)) {
            if (clazz.isAssignableFrom(String.class)) {
                return (T) jsonStr;
            }
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr, clazz);
    }

    /**
     * 指定泛型转换
     *
     * @param jsonStr json字符串
     * @return 对象
     */
    @SneakyThrows
    public static <T> T toObject(String jsonStr, TypeReference<T> typeReference) {
        if (ObjectUtil.isEmpty(jsonStr)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr, typeReference);
    }
}
