package cn.guyasc.pigeon.data.encryption.handler.object;

import cn.guyasc.pigeon.data.encryption.handler.string.StringEncHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字符串加密dto
 *
 * @author guya
 * @since 2024/1/27 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncInfo {
    /**
     * key
     */
    private String key;
    /**
     * key对应的值
     */
    private String value;
    /**
     * 加密处理器
     */
    private Class<? extends StringEncHandler> stringEncHandler;
}
