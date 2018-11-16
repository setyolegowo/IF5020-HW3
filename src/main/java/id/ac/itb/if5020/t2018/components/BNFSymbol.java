/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components;

import java.text.ParseException;
import java.util.Objects;

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

    protected BNFSymbol(String _symbol) {
        symbol = _symbol;
    }

    abstract public void match() throws ParseException;

    public static BNFSymbol create(String symbol) throws ParseException {
        switch (symbol.charAt(0)) {
            case '\\':
                return new TerminalSymbol(symbol.substring(1));
            case '<':
                return new NonTerminalSymbol(symbol);
            case '[':
                return new OptionalSymbol(symbol);
            case '{':
                return new RepetitionSymbol(symbol);
        }

        return new TerminalSymbol(symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
