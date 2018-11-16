/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018;

import java.text.ParseException;
import java.util.logging.Logger;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.specialrules.*;
import id.ac.itb.if5020.t2018.helpers.TextFileParserInterface;

/**
 *
 * @author setyo
 */
public class JavaEngine {

    public final static Logger LOGGER = Logger.getLogger(JavaEngine.class.getName());

    public static TextFileParserInterface parser;

    public static void prepareRules() throws ParseException {
        prepareRuleProgram();
        prepareRuleClass();
        prepareRuleEnum();
        prepareRuleInterface();
        prepareRuleAnnotationType();
        prepareRuleCommon();
    }

    private static void prepareRuleProgram() throws ParseException {
        BNFRule.add(
            "Program",
            rightCreator(
                "<Annotation> {<Annotation>} <EarlyAnnotation>",
                "[<PackageDeclaration>] {<ImportDeclaration>} {<TypeDeclaration>}"
            )
        );
        BNFRule.add(
            "EarlyAnnotation",
            rightCreator(
                "<PackageDeclaration> {<ImportDeclaration>} {<TypeDeclaration>}",
                "<ClassOrInterfaceDeclaration> {<TypeDeclaration>}",
                "<TypeDeclarationModifier> {<TypeDeclarationModifier>} <ClassOrInterfaceDeclaration> {<TypeDeclaration>}"
            )
        );

        // PACKAGEDECLARATION
        BNFRule.add("PackageDeclaration", rightCreator("package <QualifiedIdentifier> ;"));

        // IMPORTDECLARATION
        BNFRule.add("ImportDeclaration", rightCreator("import [static] <Identifier> <ImportDeclarationEnd>"));
        BNFRule.add("ImportDeclarationEnd", rightCreator(". <ImportDeclarationStarEnd>", ";"));
        BNFRule.add("ImportDeclarationStarEnd", rightCreator("* ;", "<Identifier> <ImportDeclarationEnd>"));

        // TYPEDECLARATION
        BNFRule.add(
            "TypeDeclaration",
            rightCreator(
                ";",
                "<ClassOrInterfaceDeclaration>",
                "<TypeDeclarationModifierWithAnnotation> <ClassOrInterfaceDeclaration>"
            )
        );
        BNFRule.add("TypeDeclarationModifierWithAnnotation", rightCreator("{<Annotation>} {<TypeDeclarationModifier>}"));
        BNFRule.add("TypeDeclarationModifier", rightCreator("public", "private", "protected", "static", "final", "abstract"));
        BNFRule.add("ClassOrInterfaceDeclaration", rightCreator("<ClassDeclaration>", "<InterfaceDeclaration>"));
        BNFRule.add("ClassDeclaration", rightCreator("<NormalClassDeclaration>", "<EnumDeclaration>"));
        BNFRule.add("InterfaceDeclaration", rightCreator("<NormalInterfaceDeclaration>", "<AnnotationTypeDeclaration>"));
    }

    private static void prepareRuleClass() throws ParseException {
        // TYPEDECLARATION CLASS
        BNFRule.add(
            "NormalClassDeclaration",
            rightCreator("class <Identifier> [<TypeParameters>] [extends <Type>] [implements <TypeList>] \\{ {<ClassBodyDeclaration>} \\}")
        );
        BNFRule.add(
            "ClassBodyDeclaration",
            rightCreator(
                ";",
                "<ModifierWithoutStatic> {<ModifierWithoutStatic>} <MemberDeclaration>",
                "<Block>",
                "static <ClassBodyDeclarationStaticPrefix>",
                "<MemberDeclaration>"
            )
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
                "<TypeParameters> <GenericMethodOrConstructorRest>",
                "<Identifier> <MemberDeclarationRest>"
            )
        );
        BNFRule.add(
            "VoidMethodDeclaratorRest",
            rightCreator(
                "<FormalParameters> [throws <QualifiedIdentifierList>] <BlockOrSemicolon>"
            )
        );
        BNFRule.add(
            "MemberDeclarationRestAfterBasic",
            rightCreator("<Identifier> <MethodOrFieldRest>")
        );
        BNFRule.add(
            "MethodDeclaratorRest",
            rightCreator(
                "<FormalParameters> {\\[ \\]} [throws <QualifiedIdentifierList>] <BlockOrSemicolon>"
            )
        );
        BNFRule.add(
            "MethodOrFieldRest",
            rightCreator(
                "<MethodDeclaratorRest>",
                "<VariableDeclaratorRest> {, <VariableDeclarator>} ;"
            )
        );
        BNFRule.add(
            "MemberDeclarationRest",
            rightCreator(
                "<TypeParameters> <MethodOrFieldRest>",
                "<ConstructorDeclaratorRest>"
            )
        );
        BNFRule.add(
            "GenericMethodOrConstructorRest",
            rightCreator(
                "void <Identifier> <MethodDeclaratorRest>",
                "<BasicType> {\\[ \\]} <MethodDeclaratorRest>",
                "<Identifier> <GenericMethodOrConstructorRestRest>"
            )
        );
        BNFRule.add("GenericMethodOrConstructorRestRest",
            rightCreator(
                "<ConstructorDeclaratorRest>",
                "[<TypeArguments>] {. <Identifier> [TypeArguments]} <Identifier> <MethodDeclaratorRest>" // Generation of ReferenceType of Type
            )
        );
        BNFRule.add(
            "ConstructorDeclaratorRest",
            rightCreator("<FormalParameters> [throws <QualifiedIdentifierList>] <Block>")
        );
    }

    private static void prepareRuleEnum() throws ParseException {
        // TYPEDECLARATION ENUM
        BNFRule.add("EnumDeclaration", rightCreator("enum <Identifier> [implements <TypeList>] <EnumBody>"));
    }

    private static void prepareRuleInterface() throws ParseException {
        // TYPEDECLARATION INTERFACE
        BNFRule.add(
            "NormalInterfaceDeclaration",
            rightCreator("interface <Identifier> [<TypeParameters>] [extends <TypeList>] <InterfaceBody>")
        );
    }

    private static void prepareRuleAnnotationType() throws ParseException {
        // TYPEDECLARATION Annotation
        BNFRule.add("AnnotationTypeDeclaration", rightCreator("@interface <Identifier> <AnnotationTypeBody>"));
    }

    private static void prepareRuleCommon() throws ParseException {
        // ANNOTATION
        BNFRule.add("Annotation", rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement",
                rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", rightCreator("= <AnnotationElementValue>"));
        // TODO Expression non terminal symbol for AnnotationElementValue
        BNFRule.add("AnnotationElementValue", rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues", rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("FormalParameters", rightCreator("( [<FormalParameterDecls>] )"));
        BNFRule.add("FormalParameterDecls", rightCreator("{<VariableModifier>} <Type> <FormalParameterDeclsRest>"));
        BNFRule.add("FormalParameterDeclsRest",
                rightCreator("... <VariableDeclaratorId>", "<VariableDeclaratorId> [, <FormalParameterDecls>]"));

        // EXPRESSION
        BNFRule.add("VariableModifier", rightCreator("final", "<Annotation>"));
        BNFRule.add("VariableDeclaratorId", rightCreator("<Identifier> {\\[ \\]}"));
        BNFRule.add("VariableDeclarator", rightCreator("<Identifier> <VariableDeclaratorRest>"));
        BNFRule.add("VariableDeclaratorRest", rightCreator("{\\[ \\]} [= <VariableInitializer>]"));

        // TOKEN
        BNFRule.add("ReferenceType", rightCreator("<Identifier> [<TypeArguments>] {. <Identifier> [<TypeArguments>]}"));
        BNFRule.add("TypeArguments", rightCreator("\\< <TypeArgument> {, <TypeArgument>} \\>"));
        BNFRule.add("TypeArgument", rightCreator("? [<ExtendsOrSuper> <ReferenceType>]", "<ReferenceType>"));

        BNFRule.add("TypeParameters", rightCreator("\\< <TypeParameter> {, <TypeParameter>} \\>"));
        BNFRule.add("TypeParameter", rightCreator("<Identifier> [extends <Bound>]"));
        BNFRule.add("Bound", rightCreator("<ReferenceType> {& <ReferenceType>}"));

        BNFRule.add("Type", rightCreator("<BasicType> {\\[ \\]}", "<ReferenceType> {\\[ \\]}"));
        BNFRule.add("BasicType", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"));

        BNFRule.add("TypeList", rightCreator("<ReferenceType> {, <ReferenceType>}"));

        BNFRule.add("ExtendsOrSuper", rightCreator("extends", "super"));
        BNFRule.add("BlockOrSemicolon", rightCreator(";", "<Block>"));

        BNFRule.add("Modifier", rightCreator("{<Annotation>} {<ModifierAfterAnnotation>}"));
        BNFRule.add("ModifierAfterAnnotation", rightCreator("public", "private", "protected", "final", "static",
                "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));

        BNFRule.add("ModifierWithoutStatic", rightCreator("{<Annotation>} {<ModifierAfterAnnotationWithoutStatic>}"));
        BNFRule.add("ModifierAfterAnnotationWithoutStatic", rightCreator("public", "private", "protected", "final",
                "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));

        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("QualifiedIdentifierList", rightCreator("<QualifiedIdentifier> {, <QualifiedIdentifier>}"));
        BNFRule.add("Identifier", new Identifier());
    }

    public static void prepareFirstList() {
        BNFRule.addFirst("Program", rightCreator("@"));
        BNFRule.addFirst("EarlyAnnotation", rightCreator("package"));
        BNFRule.addFirst("EarlyAnnotation", rightCreator("class", "enum", "interface", "@interface"));
        BNFRule.addFirst("EarlyAnnotation", rightCreator("abstract", "final", "private", "protected", "public", "static"));

        BNFRule.addFirst("PackageDeclaration", rightCreator("package"));

        BNFRule.addFirst("ImportDeclaration", rightCreator("import"));
        BNFRule.addFirst("ImportDeclarationEnd", rightCreator("."));
        BNFRule.addFirst("ImportDeclarationEnd", rightCreator(";"));
        BNFRule.addFirst("ImportDeclarationStarEnd", rightCreator("*"));
        BNFRule.addFirst("ImportDeclarationStarEnd", new Identifier());

        BNFRule.addFirst("TypeDeclaration", rightCreator(";"));
        BNFRule.addFirst("TypeDeclaration", rightCreator("class", "enum", "interface", "@interface"));
        BNFRule.addFirst("TypeDeclaration", rightCreator("@", "abstract", "final", "private", "protected", "public", "static"));
        BNFRule.addFirst("TypeDeclarationModifierWithAnnotation", rightCreator("@"));
        BNFRule.addFirst("TypeDeclarationModifierWithAnnotation", rightCreator("public", "private", "protected", "static", "final", "abstract"));
        BNFRule.addFirst("ClassOrInterfaceDeclaration", rightCreator("class", "enum"));
        BNFRule.addFirst("ClassOrInterfaceDeclaration", rightCreator("interface", "@interface"));
        BNFRule.addFirst("ClassDeclaration", rightCreator("class"));
        BNFRule.addFirst("ClassDeclaration", rightCreator("enum"));
        BNFRule.addFirst("InterfaceDeclaration", rightCreator("interface"));
        BNFRule.addFirst("InterfaceDeclaration", rightCreator("@interface"));

        BNFRule.addFirst("NormalClassDeclaration", rightCreator("class"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator(";"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("@", "public", "private", "protected", "final",
                "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("{"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("static"));
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("void"), new Identifier());
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

        BNFRule.addFirst("QualifiedIdentigier", new Identifier());
        BNFRule.addFirst("Identifier", new Identifier());
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
