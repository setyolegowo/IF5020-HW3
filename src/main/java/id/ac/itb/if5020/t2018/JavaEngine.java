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

        // PACKAGEDECLARATION
        BNFRule.add("PackageDeclaration", rightCreator("{<Annotation>} package <QualifiedIdentifier> ;"));

        // IMPORTDECLARATION
        BNFRule.add("ImportDeclaration", rightCreator("import [static] <Identifier> <ImportDeclarationEnd>"));
        BNFRule.add("ImportDeclarationEnd", rightCreator(". <ImportDeclarationStarEnd>", ";"));
        BNFRule.add("ImportDeclarationStarEnd", rightCreator("* ;", "<Identifier> <ImportDeclarationEnd>"));

        // TYPEDECLARATION
        BNFRule.add("TypeDeclaration", rightCreator(";", "[<TypeDeclarationModifierWithAnnotation>] <ClassOrInterfaceDeclaration>"));
        BNFRule.add("TypeDeclarationModifierWithAnnotation", rightCreator("{<Annotation>} {<TypeDeclarationModifier>}"));
        BNFRule.add("TypeDeclarationModifier", rightCreator("public", "private", "protected", "final", "abstract"));
        BNFRule.add("ClassOrInterfaceDeclaration", rightCreator("<ClassDeclaration>", "<InterfaceDeclaration>"));
        BNFRule.add("ClassDeclaration", rightCreator("<NormalClassDeclaration>", "<EnumDeclaration>"));
        BNFRule.add("InterfaceDeclaration", rightCreator("<NormalInterfaceDeclaration>", "<AnnotationTypeDeclaration>"));

        // TYPEDECLARATION CLASS
        BNFRule.add(
            "NormalClassDeclaration",
            rightCreator("class <Identifier> [<TypeParameters>] [extends <Type>] [implements <TypeList>] <ClassBody>")
        );
        BNFRule.add("ClassBody", rightCreator("\\{ {<ClassBodyDeclaration>} \\}"));
        BNFRule.add(
            "ClassBodyDeclaration",
            rightCreator(";", "{<ModifierWithoutStatic>} <MemberDeclaration>", "[static] <ClassBodyDeclarationStaticPrefix>")
        );
        BNFRule.add(
            "ClassBodyDeclarationStaticPrefix",
            rightCreator("<Block>", "{<ModifierWithoutStatic>} <MemberDeclaration>")
        );
        BNFRule.add(
            "MemberDeclaration",
            rightCreator(
                "<ClassOrInterfaceDeclaration>",
                "void <Identifier> <VoidMethodDeclaratorRest>",
                "<BasicType> {\\[ \\]} <MemberDeclarationRestAfterBasic>",
                "<Identifier> <MemberDeclarationRest>" // TODO NOT FINISHED YET
            )
        );
        BNFRule.add(
            "MemberDeclarationRestAfterBasic",
            rightCreator("<Identifier>") // TODO NOT FINISHED YET
        );

        // TYPEDECLARATION ENUM
        BNFRule.add("EnumDeclaration", rightCreator("enum <Identifier> [implements <TypeList>] <EnumBody>"));

        // TYPEDECLARATION INTERFACE
        BNFRule.add(
            "NormalInterfaceDeclaration",
            rightCreator("interface <Identifier> [<TypeParameters>] [extends <TypeList>] <InterfaceBody>")
        );

        // TYPEDECLARATION Annotation
        BNFRule.add("AnnotationTypeDeclaration", rightCreator("@ interface <Identifier> <AnnotationTypeBody>"));

        // ANNOTATION
        BNFRule.add("Annotation", rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement", rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", rightCreator("= <AnnotationElementValue>"));
        // TODO Expression non terminal symbol for AnnotationElementValue
        BNFRule.add("AnnotationElementValue", rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues", rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        // TOKEN
        BNFRule.add("ReferenceType", rightCreator("<Identifier> [<TypeArguments>] {. <Identifier> [<TypeArguments>]}"));
        BNFRule.add("TypeArguments", rightCreator("\\< <TypeArgument> {, <TypeArgument>} \\>"));
        BNFRule.add("TypeArgument", rightCreator("? [<ExtendsOrSuper> <ReferenceType>]", "<ReferenceType>"));

        BNFRule.add("TypeParameters", rightCreator("\\< <TypeParameter> {, <TypeParameter>} \\>"));
        BNFRule.add("TypeParameter", rightCreator("<Identifier> [extends <Bound>]"));
        BNFRule.add("Bound", rightCreator("<ReferenceType> {& <ReferenceType>}"));

        BNFRule.add("Type", rightCreator("<BasicType> {\\[ \\]}", "<ReferenceType> {\\{ \\}}"));
        BNFRule.add("BasicType", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"));

        BNFRule.add("TypeList", rightCreator("<ReferenceType> {, <ReferenceType>}"));

        BNFRule.add("ExtendsOrSuper", rightCreator("extends", "super"));

        BNFRule.add("Modifier", rightCreator("{<Annotation>} {<ModifierAfterAnnotation>}"));
        BNFRule.add(
            "ModifierAfterAnnotation",
            rightCreator(
                "public", "private", "protected", "final", "static", "abstract", "native",
                "synchronized", "transient", "volatile", "strictfp"
            )
        );

        BNFRule.add("ModifierWithoutStatic", rightCreator("{<Annotation>} {<ModifierAfterAnnotationWithoutStatic>}"));
        BNFRule.add(
            "ModifierAfterAnnotationWithoutStatic",
            rightCreator(
                "public", "private", "protected", "final", "abstract", "native", "synchronized",
                "transient", "volatile", "strictfp"
            )
        );

        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", rightCreator("<JavaLetterOrUnderscore> {<JavaLetterOrDigitOrUnderscore>}"));
        BNFRule.add("JavaLetterOrUnderscore", rightCreator("_", "<JavaLetter>"));
        BNFRule.add("JavaLetterOrDigitOrUnderscore", rightCreator("_", "<JavaLetterOrDigit>"));
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
        BNFRule.addFirst("TypeDeclarationModifierWithAnnotation", rightCreator("@", "public", "private", "protected",
                "final", "abstract"));
        BNFRule.addFirst("ClassOrInterfaceDeclaration", rightCreator("class", "enum"));
        BNFRule.addFirst("ClassOrInterfaceDeclaration", rightCreator("interface", "@"));
        BNFRule.addFirst("ClassDeclaration", rightCreator("class"));
        BNFRule.addFirst("ClassDeclaration", rightCreator("enum"));
        BNFRule.addFirst("InterfaceDeclaration", rightCreator("interface"));
        BNFRule.addFirst("InterfaceDeclaration", rightCreator("@"));

        BNFRule.addFirst("NormalClassDeclaration", rightCreator("class"));
        BNFRule.addFirst("ClassBody", rightCreator("{"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator(";"));
        // TODO Fix non LL(1) rule
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("@", "public", "private", "protected", "final",
                "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("static", "{"));
        BNFRule.addFirst("ClassBodyDeclarationStaticPrefix", rightCreator("{"));

        BNFRule.addFirst("TypeArguments", rightCreator("<"));
        BNFRule.addFirst("TypeArgument", rightCreator("?"));
        BNFRule.addFirst("TypeParameters", rightCreator("<"));

        BNFRule.addFirst("Type", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"));
        BNFRule.addFirst("BasicType", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"));

        BNFRule.addFirst("ExtendsOrSuper", rightCreator("extends"));
        BNFRule.addFirst("ExtendsOrSuper", rightCreator("super"));

        BNFRule.addFirst("Annotation", rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("{"));

        BNFRule.addFirst("TypeDeclarationModifierWithAnnotation", rightCreator("@", "public", "private", "protected", "final",
                "static", "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));

        BNFRule.addFirst("JavaLetterOrUnderscore", rightCreator("_"));
        BNFRule.addFirst("JavaLetterOrDigitOrUnderscore", rightCreator("_"));
    }

    public static String[] rightCreator(String... right) {
        return right;
    }

    public static void runProgram() throws ParseException {
        JavaEngine.parser.readNextToken();
        BNFRule rule = BNFRule.get("Program");
        rule.parse();
    }
}
