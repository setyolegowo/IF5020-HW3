/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018;

import id.ac.itb.if5020.t2018.components.BNFRule;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author setyo
 */
public class JavaEngine {

    private static HashMap<String, BNFRule> rules;

    public static void prepareRules() throws ParseException {
        rules = new HashMap<String, BNFRule>();

        for(Map.Entry<String, String[]> entry : stringRules().entrySet()) {
            rules.put(entry.getKey(), new BNFRule(entry.getKey(), entry.getValue()));
        }
    }

    private static HashMap<String, String[]> stringRules() {
        HashMap<String, String[]> _rules = new HashMap<String, String[]>();

        _rules.put("program", rightCreator("[<PackageDeclaration>] [<ImportDeclaration>] [<TypeDeclaration>]"));

        return _rules;
    }

    private static String[] rightCreator(String... right) {
        return right;
    }
}
