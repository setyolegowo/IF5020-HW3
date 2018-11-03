/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.ac.itb.if5020.t2018.components;

import java.util.List;
import java.text.ParseException;
import java.util.ArrayList;

public class BNFRule {

    public final String left;

    public final List<BNFSingleRule> rules;

    public BNFRule(String _left, String[] rights) throws ParseException {
        left = _left;
        rules = parseRight(rights);
    }

    private static List<BNFSingleRule> parseRight(String[] rights) throws ParseException {
        List<BNFSingleRule> _rules = new ArrayList<BNFSingleRule>();

        for (String right : rights) {
            _rules.add(new BNFSingleRule(right));
        }

        return _rules;
    }

    public static BNFRule create(String left, String[] rights) throws ParseException {
        return new BNFRule(left, rights);
    }
}
