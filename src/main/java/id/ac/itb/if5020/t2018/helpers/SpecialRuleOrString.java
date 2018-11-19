package id.ac.itb.if5020.t2018.helpers;

import id.ac.itb.if5020.t2018.components.SpecialRule;

public class SpecialRuleOrString {
    public final String _string;
    public final SpecialRule rule;

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

    public boolean isString() {
        return _string != null;
    }

    public boolean match(String arg) {
        if (arg == null) {
            return false;
        }
        if (arg.length() == 0) {
            return false;
        }
        if (_string != null) {
            return _string.equals(arg);
        }
        return rule.matching(arg.charAt(0));
    }
}