package cn.guyasc.pigeon.log.desensitization;

import cn.guyasc.pigeon.core.util.AssertUtil;
import cn.guyasc.pigeon.core.util.BeanLoadUtil;
import cn.guyasc.pigeon.core.util.ObjectUtil;
import cn.guyasc.pigeon.log.desensitization.rule.MatchRule;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;

/**
 * @author guya
 * @since 2023/11/23 10:23
 */
public class DefaultDesensitizationHandler implements DesensitizationHandler {
    /**
     * 脱敏规则
     */
    private final TreeSet<MatchRule> matchRuleSet;

    public DefaultDesensitizationHandler() {
        matchRuleSet = BeanLoadUtil.load(MatchRule.class);
        AssertUtil.isTrue(!ObjectUtil.isEmpty(matchRuleSet), "匹配规则为空！");
    }

    @Override
    public String execute(String content) {
        for (MatchRule matchRule : matchRuleSet) {
            Set<String> matcherSet = new HashSet<>();
            Matcher matcher = matchRule.regExp().matcher(content);
            while (matcher.find()) {
                matcherSet.add(matcher.group());
            }
            for (String result : matcherSet) {
                if (matchRule.isHide(result)) {
                    String hide = ObjectUtil.hide(result, matchRule.start(), matchRule.end());
                    content = content.replaceAll(result, hide);
                }
            }
        }
        return content;
    }

    @Override
    public boolean isPrimary() {
        return false;
    }

}
