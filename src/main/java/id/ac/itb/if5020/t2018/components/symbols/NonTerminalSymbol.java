/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components.symbols;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFSymbol;

/**
 *
 * @author setyo
 */
public class NonTerminalSymbol extends BNFSymbol {
    public NonTerminalSymbol(String _symbol) {
        super(_symbol);
    }

    @Override
    public void match() {
        JavaEngine.parser.getCurrentToken();
    }
}
