/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018;

import java.text.ParseException;
import java.util.logging.Logger;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetter;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetterOrDigit;
import id.ac.itb.if5020.t2018.helpers.TextFileParserInterface;

/**
 *
 * @author setyo
 */
public class JavaEngine {

    public final static Logger LOGGER = Logger.getLogger(JavaEngine.class.getName());

    public static TextFileParserInterface parser;

    public static void prepareRules() throws ParseException {
        BNFRule.add("Program", rightCreator("[<PackageDeclaration>] {<ImportDeclaration>}"));

        BNFRule.add("PackageDeclaration", rightCreator("{<Annotation>} package <QualifiedIdentifier> ;"));

        BNFRule.add("ImportDeclaration", rightCreator("import [static] <Identifier> <ImportDeclarationEnd>"));
        BNFRule.add("ImportDeclarationEnd", rightCreator(". <ImportDeclarationStarEnd>", ";"));
        BNFRule.add("ImportDeclarationStarEnd", rightCreator("* ;", "<Identifier> <ImportDeclarationEnd>"));

        BNFRule.add("TypeDeclaration", rightCreator(";", "<ClassDeclaration>", "<EnumDeclaration>", "<InterfaceDeclaration>", "<AnnotationDeclaration>"));

        BNFRule.add("Annotation", rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement", rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", rightCreator("= <AnnotationElementValue>"));
        BNFRule.add("AnnotationElementValue", rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues", rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", rightCreator("<JavaLetter> {<JavaLetterOrDigit>}"));
        BNFRule.add("JavaLetter", new JavaLetter());
        BNFRule.add("JavaLetterOrDigit", new JavaLetterOrDigit());
    }

    public static void prepareFirstList() {
        BNFRule.addFirst("PackageDeclaration", rightCreator("package", "@"));
        BNFRule.addFirst("ImportDeclaration", rightCreator("import"));
        BNFRule.addFirst("ImportDeclarationEnd", rightCreator("."));
        BNFRule.addFirst("ImportDeclarationEnd", rightCreator(";"));
        BNFRule.addFirst("ImportDeclarationStarEnd", rightCreator("*"));
        BNFRule.addFirst("TypeDeclaration", rightCreator(";"));

        BNFRule.addFirst("Annotation", rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("{"));
    }

    public static String[] rightCreator(String... right) {
        return right;
    }

    public static void runProgram() {
        JavaEngine.parser.readNextToken();
        BNFRule rule = BNFRule.get("Program");
        rule.parse();
    }
}
