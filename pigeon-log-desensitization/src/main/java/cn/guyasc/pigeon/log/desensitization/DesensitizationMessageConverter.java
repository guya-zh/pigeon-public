package cn.guyasc.pigeon.log.desensitization;


import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.guyasc.pigeon.core.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

/**
 * 日志脱敏处理
 *
 * @author guya
 * @since 2023/11/20 16:05
 */
@Slf4j
public class DesensitizationMessageConverter extends MessageConverter {
    private DesensitizationHandler desensitizationHandler;

    public DesensitizationMessageConverter() {
        ServiceLoader<DesensitizationHandler> load = ServiceLoader.load(DesensitizationHandler.class);
        for (DesensitizationHandler desensitizationHandler : load) {
            this.desensitizationHandler = desensitizationHandler;
            if (desensitizationHandler.isPrimary()) {
                break;
            }
        }
        AssertUtil.isTrue(this.desensitizationHandler != null, "处理器为空");
    }

    @Override
    public String convert(ILoggingEvent event) {
        return desensitizationHandler.execute(super.convert(event));
    }

}
