package cn.guyasc.pigeon.log.desensitization;

/**
 * 脱敏处理类
 *
 * @author guya
 * @since  2023/11/20 16:03
 */
public interface DesensitizationHandler {

    /**
     * 执行脱敏
     *
     * @param content 脱敏内容
     * @return 脱敏结果
     */
    String execute(String content);

    /**
     * 主要的处理类，如果没有则取第一个
     *
     * @return 是否是主要的的 true是 false不是
     */
    boolean isPrimary();
}
