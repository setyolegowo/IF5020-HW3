package id.ac.itb.if5020.t2018.helpers;

import id.ac.itb.if5020.t2018.components.SpecialRule;

public class SpecialRuleOrString {
    private final String _string;
    private final SpecialRule rule;

    public SpecialRuleOrString(String string) {
        _string = string;
        rule = null;
        if (string == null) {
            throw new RuntimeException("string cannot be null");
        }
    }

    public SpecialRuleOrString(SpecialRule arg) {
        _string = null;
        rule = arg;
        if (rule == null) {
            throw new RuntimeException("rule cannot be null");
        }
    }

    public boolean match(String arg) {
        if (_string != null) {
            return _string.equals(arg);
        }
        return rule.matching(arg.charAt(0));
    }
}