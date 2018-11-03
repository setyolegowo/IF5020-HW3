/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components;

import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;
import id.ac.itb.if5020.t2018.components.symbols.OptionalSymbol;
import id.ac.itb.if5020.t2018.components.symbols.RepetitionSymbol;
import id.ac.itb.if5020.t2018.components.symbols.TerminalSymbol;

/**
 *
 * @author setyo
 */
public abstract class BNFSymbol {

    public final String symbol;

    public BNFSymbol(String _symbol) {
        symbol = _symbol;
    }

    public static BNFSymbol create(String symbol) {
        switch (symbol.charAt(0)) {
            case '<':
                return new NonTerminalSymbol(symbol);
            case '[':
                return new OptionalSymbol(symbol);
            case '{':
                return new RepetitionSymbol(symbol);
        }

        return new TerminalSymbol(symbol);
    }
}
