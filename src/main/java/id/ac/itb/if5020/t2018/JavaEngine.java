/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.components.BNFRule;

/**
 *
 * @author setyo
 */
public class JavaEngine {

    public static void prepareRules() throws ParseException {
        BNFRule.add("program", rightCreator("[<PackageDeclaration>] [<ImportDeclaration>] [<TypeDeclaration>]"));
    }

    private static String[] rightCreator(String... right) {
        return right;
    }
}
