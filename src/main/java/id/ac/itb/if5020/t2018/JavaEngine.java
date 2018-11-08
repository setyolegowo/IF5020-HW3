/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetter;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetterOrDigit;
import id.ac.itb.if5020.t2018.helpers.TextFileParserInterface;

/**
 *
 * @author setyo
 */
public class JavaEngine {

    public static TextFileParserInterface parser;

    public static void prepareRules() throws ParseException {
        BNFRule.add("program", rightCreator("[<PackageDeclaration>]"));
        BNFRule.add("PackageDeclaration", rightCreator("package <QualifiedIdentifier> ;"));
        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> . <Identifier>"));
        BNFRule.add("Identifier", rightCreator("<JavaLetter> {<JavaLetterOrDigit>}"));
        BNFRule.add("JavaLetter", new JavaLetter());
        BNFRule.add("JavaLetterOrDigit", new JavaLetterOrDigit());
    }

    public static String[] rightCreator(String... right) {
        return right;
    }

    public static void runProgram() {
        JavaEngine.parser.readNextToken();
        BNFRule rule = BNFRule.get("program");
        rule.parse();
    }
}
