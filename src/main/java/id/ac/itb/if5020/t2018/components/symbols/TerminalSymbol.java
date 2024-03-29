/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components.symbols;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.BNFSymbol;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;

/**
 *
 * @author setyo
 */
public class TerminalSymbol extends BNFSymbol {
    public TerminalSymbol(String _symbol) {
        super(_symbol);
    }

    @Override
    public void match(BNFRule currentRule) throws ParseException {
        String token = JavaEngine.parser.getCurrentToken();
        if (symbol.equals(token)) {
            JavaEngine.parser.readNextToken();
        } else {
            throw new RuleNotMatchException("Terminal '" + symbol + "' expected", currentRule);
        }
    }
}
