/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components.symbols;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import id.ac.itb.if5020.t2018.components.BNFSymbol;
import id.ac.itb.if5020.t2018.helpers.RuleTokenization;

/**
 *
 * @author setyo
 */
public class RepetitionSymbol extends BNFSymbol {

    private final List<BNFSymbol> rules;

    public RepetitionSymbol(String _symbol) throws ParseException {
        super(_symbol);
        rules = tokenize(_symbol.substring(1, _symbol.length() - 1));
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
}
