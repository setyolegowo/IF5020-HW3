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
import java.util.List;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.helpers.Marker;
import id.ac.itb.if5020.t2018.helpers.SpecialRuleOrString;

public class BNFRule {

    private static AbstractMap<String, BNFRule> allrules = new HashMap<>();

    private static AbstractMap<String, List<List<SpecialRuleOrString>>> firsts = new HashMap<>();

    public final String left;

    public final List<BNFSingleRule> rules;

    public final SpecialRule specialSymbol;

    private BNFRule(String _left, String[] rights) throws ParseException {
        left = _left;
        rules = parseRight(rights);
        if (rules.isEmpty()) {
            throw new InvalidParameterException("BNF Rules cannot be empty");
        }
        specialSymbol = null;
    }

    private BNFRule(String _left, SpecialRule right) {
        left = _left;
        rules = null;
        specialSymbol = right;
    }

    public void parse() throws ParseException {
        if (specialSymbol != null) {
            specialSymbol.match(this);
        } else {
            List<List<SpecialRuleOrString>> firstlist = firsts.get(left);
            int i = 0;

            Marker marker = JavaEngine.parser.getMarker();
            for (BNFSingleRule rule : rules) {
                if (firstlist != null && i < firstlist.size()) {
                    boolean rsb = false;
                    for(SpecialRuleOrString rs : firstlist.get(i)) {
                        if (rs.match(marker.token)) {
                            rule.parse(this);
                            rsb = true;
                            break;
                        }
                    }
                    if (rsb) {
                        break;
                    }
                } else {
                    try {
                        rule.parse(this);
                        break;
                    } catch (RuleNotMatchException e) {
                        Marker currentMarker = JavaEngine.parser.getMarker();
                        if (i + 1 < rules.size()) {
                            if (currentMarker.hashCode() != marker.hashCode()) {
                                JavaEngine.parser.resetToMarker(marker);
                            }
                        } else {
                            throw e;
                        }
                    }
                }
                i++;
            }

            if (i == rules.size()) {
                throw new RuleNotMatchException("Token is not match with rule " + left, this);
            }
        }
    }

    private static List<BNFSingleRule> parseRight(String[] rights) throws ParseException {
        List<BNFSingleRule> _rules = new ArrayList<>();

        for (String right : rights) {
            _rules.add(new BNFSingleRule(right));
        }

        return _rules;
    }

    public static void add(String left, String[] rights) throws ParseException {
        allrules.put(left, new BNFRule(left, rights));
    }

    public static void addFirst(String left, String[] terminals) {
        List<List<SpecialRuleOrString>> first;
        if (!firsts.containsKey(left)) {
            first = new ArrayList<List<SpecialRuleOrString>>();
            firsts.put(left, first);
        } else {
            first = firsts.get(left);
        }

        List<SpecialRuleOrString> _terminals = new ArrayList<>();
        for (String terminal : terminals) {
            _terminals.add(new SpecialRuleOrString(terminal));
        }
        first.add(_terminals);
    }

    public static void addFirst(String left, String[] terminals, SpecialRule... specialRules) {
        List<List<SpecialRuleOrString>> first;
        if (!firsts.containsKey(left)) {
            first = new ArrayList<List<SpecialRuleOrString>>();
            firsts.put(left, first);
        } else {
            first = firsts.get(left);
        }

        List<SpecialRuleOrString> _terminals = new ArrayList<>();
        for (String terminal : terminals) {
            _terminals.add(new SpecialRuleOrString(terminal));
        }
        if (specialRules != null) {
            for(SpecialRule newrule : specialRules) {
                _terminals.add(new SpecialRuleOrString(newrule));
            }
        }
        first.add(_terminals);
    }

    public static void addFirst(String left, SpecialRule... specialRules) {
        List<List<SpecialRuleOrString>> first;
        if (!firsts.containsKey(left)) {
            first = new ArrayList<List<SpecialRuleOrString>>();
            firsts.put(left, first);
        } else {
            first = firsts.get(left);
        }

        List<SpecialRuleOrString> _terminals = new ArrayList<>();
        if (specialRules != null) {
            for (SpecialRule newrule : specialRules) {
                _terminals.add(new SpecialRuleOrString(newrule));
            }
        }
        first.add(_terminals);
    }

    public static void add(String left, SpecialRule right) {
        allrules.put(left, new BNFRule(left, right));
    }

    public static BNFRule get(String left) {
        return allrules.get(left);
    }

    public static void clear() {
        allrules.clear();
        firsts.clear();
    }
}
