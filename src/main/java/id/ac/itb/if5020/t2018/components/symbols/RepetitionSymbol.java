/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components.symbols;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFSymbol;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.specialrules.SpecialRuleFinish;
import id.ac.itb.if5020.t2018.helpers.Marker;
import id.ac.itb.if5020.t2018.helpers.RuleTokenization;

/**
 *
 * @author setyo
 */
public class RepetitionSymbol extends BNFSymbol {

    private final List<BNFSymbol> rules;

    public RepetitionSymbol(String _symbol) throws ParseException {
        super(_symbol);
        if (_symbol.charAt(0) != '{' || _symbol.charAt(_symbol.length() - 1) != '}') {
            throw new InvalidParameterException("Symbol must start with '{' and end with '}'");
        }
        rules = tokenize(_symbol.substring(1, _symbol.length() - 1));
        if (rules.isEmpty()) {
            throw new InvalidParameterException("Rules inside must be not empty");
        }
    }

    private static List<BNFSymbol> tokenize(String _rule) throws ParseException {
        String token;
        RuleTokenization tokenization = new RuleTokenization(_rule);
        List<BNFSymbol> _tokenRule = new ArrayList<BNFSymbol>();

        while (tokenization.hasNext()) {
            token = tokenization.readNextToken();
            _tokenRule.add(BNFSymbol.create(token));
        }

        return _tokenRule;
    }

    @Override
    public void match() throws ParseException {
        boolean traceback = false;
        Marker marker = null;

        try {
            while (true) {
                traceback = false;
                marker = JavaEngine.parser.getMarker();
                for (BNFSymbol symbol : rules) {
                    symbol.match();
                    traceback = true;
                }
            }
        } catch (RuleNotMatchException e) {
            JavaEngine.parser.markError();
            if (traceback) {
                JavaEngine.parser.resetToMarker(marker);
            }
        } catch (SpecialRuleFinish e) {}
    }
}
