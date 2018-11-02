/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018.components;

/**
 *
 * @author setyo
 */
public class BNFRule {
    
    public final String left;
    
    public BNFRule(String _left, String[] right) {
        left = _left;
    }
    
    public static BNFRule create(String left, String[] right) {
        return new BNFRule(left, right);
    }
}
