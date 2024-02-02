package cn.guyasc.pigeon.log.desensitization.rule;

import java.util.regex.Pattern;

/**
 * 手机号匹配规则
 *
 * @author guya
 * @since 2023/11/20 17:17
 */
public class PhoneMatchRule implements MatchRule {
    private final Pattern pattern = Pattern.compile("(?<!\\d)(((13)|(14)|(15)|(16)|(17)|(18)|(19))\\d{9})(?!\\d)");

    @Override
    public Pattern regExp() {
        return pattern;
    }

    @Override
    public int start() {
        return 3;
    }

    @Override
    public int end() {
        return 6;
    }

    @Override
    public boolean isHide(String content) {
        return true;
    }

    @Override
    public float sort() {
        return 10000F;
    }
}
