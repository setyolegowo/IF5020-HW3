/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components.symbols;

import java.security.InvalidParameterException;
import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.BNFSymbol;

/**
 *
 * @author setyo
 */
public class NonTerminalSymbol extends BNFSymbol {

    private final String ruleName;

    public NonTerminalSymbol(String _symbol) {
        super(_symbol);
        if (_symbol.charAt(0) != '<' || _symbol.charAt(_symbol.length() - 1) != '>') {
            throw new InvalidParameterException("Symbol must start with '<' and end with '>'");
        }
        ruleName = symbol.substring(1, _symbol.length() - 1);
    }

    @Override
    public void match() throws ParseException {
        BNFRule rule = BNFRule.get(ruleName);
        if (rule == null) {
            throw new ParseException("Rule " + ruleName + " not found.", JavaEngine.parser.getCurrentCol());
        }
        rule.parse();
    }
}
