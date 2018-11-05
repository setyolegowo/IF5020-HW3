/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.ac.itb.if5020.t2018.components;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BNFRule {

    private static AbstractMap<String, BNFRule> allrules = new HashMap<>();

    public final String left;

    public final List<BNFSingleRule> rules;

    private BNFRule(String _left, String[] rights) throws ParseException {
        left = _left;
        rules = parseRight(rights);
        if (rules.isEmpty()) {
            throw new InvalidParameterException("BNF Rules cannot be empty");
        }
    }

    private static List<BNFSingleRule> parseRight(String[] rights) throws ParseException {
        List<BNFSingleRule> _rules = new ArrayList<>();

        for (String right : rights) {
            _rules.add(new BNFSingleRule(right));
        }

        return _rules;
    }

    /**
     * Caching first list.
     */
    private Set<String> firstList;

    public Set<String> getFirst() {
        if (firstList == null) {
            firstList = new HashSet<>();
            for (BNFSingleRule rule : rules) {
                if (rule.first() != null) {
                    for (String first : rule.first()) {
                        firstList.add(first);
                    }
                }
            }
        }

        return firstList;
    }

    public static void add(String left, String[] rights) throws ParseException {
        allrules.put(left, new BNFRule(left, rights));
    }

    public static BNFRule get(String left) {
        return allrules.get(left);
    }
}
