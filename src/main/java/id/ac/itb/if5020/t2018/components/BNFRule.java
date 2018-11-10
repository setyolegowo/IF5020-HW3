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

public class BNFRule {

    private static AbstractMap<String, BNFRule> allrules = new HashMap<>();

    private static AbstractMap<String, List<List<String>>> firsts = new HashMap<>();

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

    public void parse() {
        if (specialSymbol != null) {
            specialSymbol.match();
        } else {
            List<List<String>> firstlist = firsts.get(left);
            List<String> list;
            int i = 0;

            Marker marker = JavaEngine.parser.getMarker();
            for (BNFSingleRule rule : rules) {
                if (firstlist != null && i < firstlist.size()) {
                    list = firstlist.get(i);
                    if (list.indexOf(marker.token) >= 0) {
                        rule.parse();
                        break;
                    }
                } else {
                    try {
                        rule.parse();
                        break;
                    } catch (RuleNotMatchException e) {
                        if (i + 1 < rules.size()) {
                            JavaEngine.parser.resetToMarker(marker);
                        } else {
                            throw e;
                        }
                    }
                }
                i++;
            }

            if (i == rules.size()) {
                throw new RuleNotMatchException("Token is not match with rule " + left);
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
        List<List<String>> first;
        if (!firsts.containsKey(left)) {
            first = new ArrayList<List<String>>();
            firsts.put(left, first);
        } else {
            first = firsts.get(left);
        }

        List<String> _terminals = new ArrayList<>();
        for (String terminal : terminals) {
            _terminals.add(terminal);
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
    }
}
