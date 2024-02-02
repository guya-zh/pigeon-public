package cn.guyasc.pigeon.log.desensitization.rule;

import java.util.regex.Pattern;

/**
 * 匹配规则
 *
 * @author guya
 * @since 2023/11/20 15:15
 */
public interface MatchRule extends Comparable<MatchRule> {

    /**
     * 匹配敏感数据正则表达式
     *
     * @return 正则表达式
     */
    Pattern regExp();

    /**
     * 开始位置
     *
     * @return 开始位置
     */
    int start();

    /**
     * 结束位置
     *
     * @return 结束位置
     */
    int end();

    /**
     * 判断是否需要脱敏感
     */
    boolean isHide(String content);

    /**
     * 顺序,越小执行优先级越高
     */
    float sort();

    @Override
    default int compareTo(MatchRule o) {
        int compare = Float.compare(this.sort(), o.sort());
        return compare == 0 ? Integer.compare(this.hashCode(), o.hashCode()) : compare;
    }
}
