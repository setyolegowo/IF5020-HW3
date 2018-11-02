/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components;

import id.ac.itb.if5020.t2018.components.symbols.TerminalSymbol;

/**
 *
 * @author setyo
 */
public abstract class BNFSymbol {
    
    public static BNFSymbol create() {
        return new TerminalSymbol();
    }
}
