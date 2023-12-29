package cn.guyasc.pigeon.log.desensitization.rule;

import java.util.regex.Pattern;

/**
 * 身份证匹配规则
 *
 * @author guya
 * @since 2023/11/20 17:17
 */
public class IdCardMatchRule implements MatchRule {
    private final Pattern pattern = Pattern.compile("(?<!\\d)(([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])(?!\\d)");

    @Override
    public Pattern regExp() {
        return pattern;
    }

    @Override
    public int start() {
        return 1;
    }

    @Override
    public int end() {
        return 16;
    }

    @Override
    public float sort() {
        return 8000F;
    }
}
