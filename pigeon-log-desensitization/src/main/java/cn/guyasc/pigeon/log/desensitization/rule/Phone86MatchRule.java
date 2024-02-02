package cn.guyasc.pigeon.log.desensitization.rule;

import java.util.regex.Pattern;

/**
 * +86这种格式的手机号
 *
 * @author guya
 * @since 2023/11/20 17:17
 */
public class Phone86MatchRule implements MatchRule {
    private final Pattern pattern = Pattern.compile("(?<!\\d)86(((13)|(14)|(15)|(16)|(17)|(18)|(19))\\d{9})(?!\\d)");

    @Override
    public Pattern regExp() {
        return pattern;
    }

    @Override
    public int start() {
        return 5;
    }

    @Override
    public int end() {
        return 8;
    }

    @Override
    public boolean isHide(String content) {
        return true;
    }

    @Override
    public float sort() {
        return 9000F;
    }
}
