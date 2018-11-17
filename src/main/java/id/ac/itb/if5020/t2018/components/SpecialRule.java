package id.ac.itb.if5020.t2018.components;

import java.text.ParseException;

/**
 * SpecialRule
 */
public interface SpecialRule {
    public void match(BNFRule currentRule) throws ParseException;
    public boolean matching(char _char);
}