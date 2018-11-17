package id.ac.itb.if5020.t2018.components;

public class RuleNotMatchException extends RuntimeException {

    private static final long serialVersionUID = -3171115946995659298L;

    public final BNFRule rule;

    public RuleNotMatchException(String arg0, BNFRule arg1) {
        super(arg0);
        rule = arg1;
        if (rule == null) {
            throw new RuntimeException("Rule error cannot be null");
        }
    }

}