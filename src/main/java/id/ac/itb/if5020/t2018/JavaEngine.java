/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.if5020.t2018;

import java.text.ParseException;
import java.util.logging.Logger;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.specialrules.*;
import id.ac.itb.if5020.t2018.helpers.TextFileParserInterface;

/**
 *
 * @author setyo
 */
public class JavaEngine {

    public static boolean throwNonLL1 = false;

    public final static Logger LOGGER = Logger.getLogger(JavaEngine.class.getName());

    public static TextFileParserInterface parser;

    public static void prepareRules() throws ParseException {
        prepareRuleProgram();
        prepareRuleClass();
        prepareRuleEnum();
        prepareRuleInterface();
        prepareRuleAnnotationType();
        prepareRuleCommon();
		prepareRuleBlock();
		prepareRuleExpression();
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
                "<ModifierWithoutStatic> {<ModifierAfterAnnotation>} <MemberDeclaration>",
                "<Block>",
                "static <ClassBodyDeclarationStaticPrefix>",
                "<MemberDeclaration>"
            )
        );
        BNFRule.add(
            "ClassBodyDeclarationStaticPrefix",
            rightCreator(
                "<Block>",
                "<ModifierAfterAnnotationWithoutStatic> {<ModifierAfterAnnotationWithoutStatic>} <MemberDeclaration>",
                "<MemberDeclaration>"
            )
        );
        BNFRule.add(
            "MemberDeclaration",
            rightCreator(
                "<ClassOrInterfaceDeclaration>",
                "void <Identifier> <VoidMethodDeclaratorRest>",
                "<BasicType> {\\[]} <MemberDeclarationRestAfterBasic>",
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
                "<FormalParameters> {\\[]} [throws <QualifiedIdentifierList>] <BlockOrSemicolon>"
            )
        );
        BNFRule.add(
            "MethodOrFieldRest",
            rightCreator(
                "<MethodDeclaratorRest>",
                "<VariableDeclaratorRest> {, <VariableDeclarator>} ;",
                ", <VariableDeclarator> {, <VariableDeclarator>} ;",
                ";"
            )
        );
        BNFRule.add(
            "MemberDeclarationRest",
            rightCreator(
                "<TypeArguments> {\\[]} <Identifier> <MethodOrFieldRest>",
                "<ConstructorDeclaratorRest>",
                "\\[] {\\[]} <Identifier> <MethodOrFieldRest>",
                "<Identifier> <MethodOrFieldRest>"
            )
        );
        BNFRule.add(
            "GenericMethodOrConstructorRest",
            rightCreator(
                "void <Identifier> <MethodDeclaratorRest>",
                "<BasicType> {\\[]} <MethodDeclaratorRest>",
                "<Identifier> <GenericMethodOrConstructorRestRest>"
            )
        );
        BNFRule.add("GenericMethodOrConstructorRestRest",
            rightCreator(
                "<ReferenceTypeRest> {\\[]} <Identifier> <MethodDeclaratorRest>",
                "\\[] {\\[]} <Identifier> <MethodDeclaratorRest>",
                "<ConstructorDeclaratorRest>",
                "<Identifier> <MethodDeclaratorRest>" // Generation of ReferenceType of Type
            )
        );
        BNFRule.add(
            "ConstructorDeclaratorRest",
            rightCreator("<FormalParameters> [throws <QualifiedIdentifierList>] <Block>")
        );
    }

    private static void prepareRuleEnum() throws ParseException {
        // TYPEDECLARATION ENUM
        BNFRule.add("EnumDeclaration", rightCreator("enum <Identifier> [implements <TypeList>] " +
                "\\{ [<EnumConstant>] [,] [<EnumBodyDeclarations>] \\}"));

        BNFRule.add("EnumConstants", rightCreator("<EnumConstant> {, <EnumConstant>}"));
        BNFRule.add("EnumConstant", rightCreator(
            "<Annotation> <Identifier> [<Arguments>] [\\{ <ClassBodyDeclaration> \\}]",
            "<Identifier> [Arguments] [\\{ <ClassBodyDeclaration> \\}]"
        ));

        BNFRule.add("EnumBodyDeclarations", rightCreator("; {<ClassBodyDeclaration>}"));
    }

    private static void prepareRuleInterface() throws ParseException {
        // TYPEDECLARATION INTERFACE
        BNFRule.add(
            "NormalInterfaceDeclaration",
            rightCreator("interface <Identifier> [<TypeParameters>] [extends <TypeList>] " +
                "\\{ {<InterfaceBodyDeclaration>} \\}")
        );

        BNFRule.add(
            "NormalInterfaceDeclaration",
            rightCreator(
                ";",
                "<Modifier> {<Modifier>} InterfaceMemberDecl",
                "InterfaceMemberDecl"
            )
        );
        BNFRule.add(
            "NormalInterfaceDeclaration",
            rightCreator(
                "void <Identifier> <VoidInterfaceMethodDeclaratorRest>",
                "<TypeParameters> <InterfaceGenericMethodDeclRest>",
                "<ClassDeclaration>",
                "<InterfaceDeclaration>",
                "<InterfaceMethodOrFieldDecl>"
            )
        );
        BNFRule.add("InterfaceMethodOrFieldDecl", rightCreator("<Type> <Identifier> <InterfaceMethodOrFieldRest>"));
        BNFRule.add("InterfaceMethodOrFieldRest", rightCreator(
            "<ContantDeclaratorRest> {, <ContantDeclaratorRest>}",
            "<InterfaceMethodDeclaratorRest>"
        ));
        BNFRule.add("ConstantDeclaratorRest", rightCreator(
            "= <VariableInitializer>",
            "\\[] {\\[]} = <VariableInitializer>"
        ));
        BNFRule.add("ConstantDeclarator", rightCreator(
            "<Identifier> <ConstantDeclaratorRest>"
        ));
        BNFRule.add("InterfaceMethodDeclaratorRest", rightCreator(
            "<FormalParameters> {\\[]} [throws <QualifiedIdentifierList>] ;"
        ));
        BNFRule.add("VoidInterfaceMethodDeclaratorRest", rightCreator(
            "<FormalParameters> [throws <QualifiedIdentifierList>] ;"
        ));
        BNFRule.add("InterfaceGenericMethodDeclRest", rightCreator(
            "<TypeParameters> <TypeOrVoid> <Identifier> <InterfaceMethodDeclaratorRest> ;"
        ));
    }

    private static void prepareRuleAnnotationType() throws ParseException {
        // TYPEDECLARATION Annotation
        BNFRule.add("AnnotationTypeDeclaration", rightCreator("@interface <Identifier> \\{ [AnnotationTypeElementDeclarations] \\}"));

        BNFRule.add(
            "AnnotationTypeElementDeclarations",
            rightCreator(
                "<AnnotationTypeElementRest> [<AnnotationTypeElementDeclarations>]",
                "<Modifier> {<Modifier>} <AnnotationTypeElementRest> [<AnnotationTypeElementDeclarations>]"
            )
        );
        BNFRule.add(
            "AnnotationTypeElementRest",
            rightCreator(
                "<Type> <Identifier> <AnnotationMethodOrConstantRest> ;",
                "<ClassDeclaration>",
                "<IntefaceDeclaration>",
                "<EnumDeclaration>",
                "<AnnotationTypeDeclaration>"
            )
        );
        BNFRule.add("AnnotationMethodOrConstantRest", rightCreator("( ) [\\[]] [default <ElementValue>]"));
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
        BNFRule.add("VariableDeclarators", rightCreator("<VariableDeclarator> {, <VariableDeclarator>}"));
        BNFRule.add("VariableDeclaratorId", rightCreator("<Identifier> {\\[ \\]}"));
        BNFRule.add("VariableDeclarator", rightCreator("<Identifier> [<VariableDeclaratorRest>]"));
        BNFRule.add("VariableDeclaratorRest", rightCreator("\\[] {\\[]} [= <VariableInitializer>]", "= <VariableInitializer>"));
        BNFRule.add("VariableInitializer", rightCreator("<ArrayInitializer>", "<Expression>"));
        BNFRule.add("ArrayInitializer", rightCreator("\\{ [<VariableInitializer> {, <VariableInitializer>}] \\}"));

        // TOKEN
        BNFRule.add("ReferenceType", rightCreator("<Identifier> [<ReferenceTypeRest>]"));
        BNFRule.add("ReferenceTypeRest", rightCreator("<TypeArguments> [. <Identifier> [ReferenceTypeRest]]", ". Identifier [ReferenceTypeRest]"));
        BNFRule.add("TypeArguments", rightCreator("\\< <TypeArgument> {, <TypeArgument>} \\>"));
        BNFRule.add("TypeArgument", rightCreator("? [<ExtendsOrSuper> <ReferenceType>]", "<ReferenceType>"));

        BNFRule.add("TypeParameters", rightCreator("\\< <TypeParameter> {, <TypeParameter>} \\>"));
        BNFRule.add("TypeParameter", rightCreator("<Identifier> [extends <Bound>]"));
        BNFRule.add("Bound", rightCreator("<ReferenceType> {& <ReferenceType>}"));

        BNFRule.add("Type", rightCreator("<BasicType> {\\[]}", "<ReferenceType> {\\[]}"));
        BNFRule.add("BasicType", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"));

        BNFRule.add("TypeList", rightCreator("<ReferenceType> {, <ReferenceType>}"));

        BNFRule.add("ExtendsOrSuper", rightCreator("extends", "super"));
        BNFRule.add("BlockOrSemicolon", rightCreator(";", "<Block>"));

        BNFRule.add("Modifier", rightCreator(
            "<Annotation> {<Annotation>} {<ModifierAfterAnnotation>}",
            "<ModifierAfterAnnotation> {<ModifierAfterAnnotation>}"
        ));
        BNFRule.add("ModifierAfterAnnotation", rightCreator("public", "private", "protected", "final", "static",
                "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));

        BNFRule.add("ModifierWithoutStatic", rightCreator(
            "<Annotation> {<Annotation>} {<ModifierAfterAnnotationWithoutStatic>}",
            "<ModifierAfterAnnotationWithoutStatic> {<ModifierAfterAnnotationWithoutStatic>}"
        ));
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
        BNFRule.addFirst("TypeDeclarationModifier", rightCreator("public"));
        BNFRule.addFirst("TypeDeclarationModifier", rightCreator("private"));
        BNFRule.addFirst("TypeDeclarationModifier", rightCreator("protected"));
        BNFRule.addFirst("TypeDeclarationModifier", rightCreator("static"));
        BNFRule.addFirst("TypeDeclarationModifier", rightCreator("final"));
        BNFRule.addFirst("TypeDeclarationModifier", rightCreator("abstract"));
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
        BNFRule.addFirst("ClassBodyDeclaration", rightCreator("class", "enum", "interface",
                "@interface", "void", "boolean", "byte", "char", "double", "float", "int", "long", "short",
                "<"), new Identifier());
        BNFRule.addFirst("ClassBodyDeclarationStaticPrefix", rightCreator("{"));
        BNFRule.addFirst("ClassBodyDeclarationStaticPrefix", rightCreator("public", "private", "protected", "final",
                "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));
        BNFRule.addFirst("ClassBodyDeclarationStaticPrefix", rightCreator("class", "enum", "interface",
                "@interface", "void", "boolean", "byte", "char", "double", "float", "int", "long", "short",
                "<"), new Identifier());

        BNFRule.addFirst("MemberDeclaration", rightCreator("class", "enum", "interface", "@interface"));
        BNFRule.addFirst("MemberDeclaration", rightCreator("void"));
        BNFRule.addFirst("MemberDeclaration", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short"));
        BNFRule.addFirst("MemberDeclaration", rightCreator("<"));
        BNFRule.addFirst("MemberDeclaration", new Identifier());

        BNFRule.addFirst("VoidMethodDeclarationRest", rightCreator("("));
        BNFRule.addFirst("MemberDeclarationRestAfterBasic", new Identifier());
        BNFRule.addFirst("MethodDeclaratorRest", rightCreator("("));
        BNFRule.addFirst("MethodOrFieldRest", rightCreator("("));
        BNFRule.addFirst("MethodOrFieldRest", rightCreator("=", "[]"));
        BNFRule.addFirst("MethodOrFieldRest", rightCreator(","));
        BNFRule.addFirst("MethodOrFieldRest", rightCreator(";"));

        BNFRule.addFirst("MemberDeclarationRest", rightCreator("<"));
        BNFRule.addFirst("MemberDeclarationRest", rightCreator("("));
        BNFRule.addFirst("MemberDeclarationRest", rightCreator("[]"));
        BNFRule.addFirst("MemberDeclarationRest", new Identifier());

        BNFRule.addFirst("GenericMethodOrConstructorRest", rightCreator("void"));
        BNFRule.addFirst("GenericMethodOrConstructorRest",
                rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short"));
        BNFRule.addFirst("GenericMethodOrConstructorRest", new Identifier());

        BNFRule.addFirst("GenericMethodOrConstructorRestRest", rightCreator(".", "<"));
        BNFRule.addFirst("GenericMethodOrConstructorRestRest", rightCreator("[]"));
        BNFRule.addFirst("GenericMethodOrConstructorRestRest", rightCreator("("));
        BNFRule.addFirst("GenericMethodOrConstructorRestRest", new Identifier());

        BNFRule.addFirst("ConstructorDeclaratorRest", rightCreator("("));

        BNFRule.addFirst("EnumDeclaration", rightCreator("enum"));
        BNFRule.addFirst("EnumConstants", rightCreator("@"), new Identifier());
        BNFRule.addFirst("EnumConstant", rightCreator("@"));
        BNFRule.addFirst("EnumConstant", new Identifier());
        BNFRule.addFirst("EnumBodyDeclarations", rightCreator(";"));

        BNFRule.addFirst("NormalInterfaceDeclaration", rightCreator("interface"));
        BNFRule.addFirst("InterfaceBodyDeclaration", rightCreator(";"));
        BNFRule.addFirst("InterfaceBodyDeclaration", rightCreator("@", "public", "private", "protected", "final",
                "abstract", "static", "native", "synchronized", "transient", "volatile", "strictfp"));
        BNFRule.addFirst("InterfaceBodyDeclaration", rightCreator("void", "<", "class", "interface", "@interface",
                "boolean", "byte", "char", "double", "float", "int", "long", "short"), new Identifier());
        BNFRule.addFirst("InterfaceMemberDecl", rightCreator("void"));
        BNFRule.addFirst("InterfaceMemberDecl", rightCreator("<"));
        BNFRule.addFirst("InterfaceMemberDecl", rightCreator("class", "enum"));
        BNFRule.addFirst("InterfaceMemberDecl", rightCreator("interface", "@interface"));
        BNFRule.addFirst("InterfaceMemberDecl", rightCreator("boolean", "byte", "char",
                "double", "float", "int", "long", "short"), new Identifier());
        BNFRule.addFirst("InterfaceMethodOrFieldDecl", rightCreator("boolean", "byte", "char",
                "double", "float", "int", "long", "short"), new Identifier());

        BNFRule.addFirst("InterfaceMethodOrFieldRest", rightCreator("=", "[]"));
        BNFRule.addFirst("InterfaceMethodOrFieldRest", rightCreator("("));
        BNFRule.addFirst("ConstantDeclaratorRest", rightCreator("="));
        BNFRule.addFirst("ConstantDeclaratorRest", rightCreator("[]"));
        BNFRule.addFirst("ConstantDeclarator", new Identifier());
        BNFRule.addFirst("InterfaceMethodDeclaratorRest", rightCreator("("));
        BNFRule.addFirst("VoidInterfaceMethodDeclaratorRest", rightCreator("("));
        BNFRule.addFirst("InterfaceGenericMethodDeclRest", rightCreator("<"));

        BNFRule.addFirst("AnnotationTypeDeclaration", rightCreator("@interface"));
        BNFRule.addFirst("AnnotationTypeElementDeclarations", rightCreator("class", "interface",
                "enum", "@interface", "boolean", "byte", "char", "double", "float", "int", "long",
                "short"), new Identifier());
        BNFRule.addFirst("AnnotationTypeElementDeclarations", rightCreator("@", "public", "private",
                "protected", "final", "abstract", "static", "native", "synchronized", "transient",
                "volatile", "strictfp"));
        BNFRule.addFirst("AnnotationTypeElementRest", rightCreator("boolean", "byte", "char",
                "double", "float", "int", "long", "short"), new Identifier());
        BNFRule.addFirst("AnnotationTypeElementRest", rightCreator("class"));
        BNFRule.addFirst("AnnotationTypeElementRest", rightCreator("interface"));
        BNFRule.addFirst("AnnotationTypeElementRest", rightCreator("enum"));
        BNFRule.addFirst("AnnotationTypeElementRest", rightCreator("@interface"));
        BNFRule.addFirst("AnnotationMethodOrConstantRest", rightCreator("("));

        BNFRule.addFirst("TypeArguments", rightCreator("<"));
        BNFRule.addFirst("TypeArgument", rightCreator("?"));
        BNFRule.addFirst("TypeParameters", rightCreator("<"));

        BNFRule.addFirst("Type", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"));
        BNFRule.addFirst("BasicType", rightCreator("byte"));
		BNFRule.addFirst("BasicType", rightCreator("short"));
		BNFRule.addFirst("BasicType", rightCreator("char"));
		BNFRule.addFirst("BasicType", rightCreator("int"));
		BNFRule.addFirst("BasicType", rightCreator("long"));
		BNFRule.addFirst("BasicType", rightCreator("float"));
		BNFRule.addFirst("BasicType", rightCreator("double"));
		BNFRule.addFirst("BasicType", rightCreator("boolean"));

        BNFRule.addFirst("ExtendsOrSuper", rightCreator("extends"));
        BNFRule.addFirst("ExtendsOrSuper", rightCreator("super"));

        BNFRule.addFirst("Annotation", rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("{"));

        BNFRule.addFirst("FormalParameters", rightCreator("("));
        BNFRule.addFirst("FormalParametersDecls", rightCreator("@", "final"));
        BNFRule.addFirst("FormalParametersDecls", rightCreator("byte", "short", "char", "int", "long", "float", "double", "boolean"), new Identifier());
        BNFRule.addFirst("FormalParametersDeclsRest", rightCreator("..."));
        BNFRule.addFirst("FormalParametersDeclsRest", new Identifier());

        BNFRule.addFirst("VariableModifier", rightCreator("final"));
        BNFRule.addFirst("VariableModifier", rightCreator("@"));

        BNFRule.addFirst("VariableDeclarators", new Identifier());
        BNFRule.addFirst("VariableDeclaratorId", new Identifier());
        BNFRule.addFirst("VariableDeclarator", new Identifier());
        BNFRule.addFirst("VariableDeclaratorRest", rightCreator("[]"));
        BNFRule.addFirst("VariableDeclaratorRest", rightCreator("="));
        BNFRule.addFirst("VariableInitializer", rightCreator("{"));
        BNFRule.addFirst("VariableInitializer", rightCreator("++", "--", "!", "~", "+", "-", "("), new Literal());
        BNFRule.addFirst("ArrayInitializer", rightCreator("{"));

        BNFRule.addFirst("Modifier", rightCreator("@"));
        BNFRule.addFirst("Modifier", rightCreator("public", "private", "protected", "final",
                "abstract", "static", "native", "synchronized", "transient", "volatile", "strictfp"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("public"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("private"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("protected"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("final"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("static"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("abstract"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("native"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("synchronized"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("transient"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("volatile"));
        BNFRule.addFirst("ModifierAfterAnnotation", rightCreator("strictfp"));
        BNFRule.addFirst("ModifierWithoutStatic", rightCreator("@"));
        BNFRule.addFirst("ModifierWithoutStatic", rightCreator("public", "private", "protected", "final", "abstract",
                "native", "synchronized", "transient", "volatile", "strictfp"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("public"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("private"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("protected"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("final"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("abstract"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("native"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("synchronized"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("transient"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("volatile"));
        BNFRule.addFirst("ModifierAfterAnnotationWithoutStatic", rightCreator("strictfp"));

        BNFRule.addFirst("TypeDeclarationModifierWithAnnotation", rightCreator("@", "public", "private", "protected", "final",
                "static", "abstract", "native", "synchronized", "transient", "volatile", "strictfp"));

        BNFRule.addFirst("QualifiedIdentigier", new Identifier());
        BNFRule.addFirst("Identifier", new Identifier());

		// Block
		BNFRule.addFirst("Block", rightCreator("{"));
		BNFRule.addFirst("BlockStatement", rightCreator("class", "enum"));
		BNFRule.addFirst("BlockStatement", rightCreator("{", ";", "if", "assert", "switch", "while", "do", "for", "break", "continue", "return", "throw", "synchronized", "try", "++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("BlockStatement", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short", "final", "@"), new Identifier());
		BNFRule.addFirst("LocalVariableDeclarationStatement", rightCreator("final", "@"));
		BNFRule.addFirst("LocalVariableDeclarationStatement", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short"), new Identifier());
		BNFRule.addFirst("VariableInitializer", rightCreator("["));
		BNFRule.addFirst("VariableInitializer", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("Statement", rightCreator("{"));
		BNFRule.addFirst("Statement", rightCreator(";"));
		BNFRule.addFirst("Statement", rightCreator("if"));
		BNFRule.addFirst("Statement", rightCreator("assert"));
		BNFRule.addFirst("Statement", rightCreator("switch"));
		BNFRule.addFirst("Statement", rightCreator("while"));
		BNFRule.addFirst("Statement", rightCreator("do"));
		BNFRule.addFirst("Statement", rightCreator("for"));
		BNFRule.addFirst("Statement", rightCreator("break"));
		BNFRule.addFirst("Statement", rightCreator("continue"));
		BNFRule.addFirst("Statement", rightCreator("return"));
		BNFRule.addFirst("Statement", rightCreator("throw"));
		BNFRule.addFirst("Statement", rightCreator("synchronized"));
		BNFRule.addFirst("Statement", rightCreator("try"));
		BNFRule.addFirst("Statement", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("SwitchBlockStatementGroup", rightCreator("case", "default"));
		BNFRule.addFirst("SwitchLabel", rightCreator("case"));
		BNFRule.addFirst("SwitchLabel", rightCreator("default"));
		BNFRule.addFirst("SwitchLabelCase", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("SwitchLabelCase", new Identifier());
		BNFRule.addFirst("ForControl", rightCreator(";"));
		BNFRule.addFirst("ForControl", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("ForControl", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short", "final", "@"), new Identifier());
		BNFRule.addFirst("ForVarControl", rightCreator("final", "@"));
		BNFRule.addFirst("ForVarControl", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short"), new Identifier());
		BNFRule.addFirst("ForVarControlRest", rightCreator(";"));
		BNFRule.addFirst("ForVarControlRest", rightCreator("=", ","));
		BNFRule.addFirst("ForVarControlRest", rightCreator(":"));
		BNFRule.addFirst("ForVariableDeclaratorsRest", rightCreator("="));
		BNFRule.addFirst("ForVariableDeclaratorsRest", rightCreator(","));
		BNFRule.addFirst("ForInit", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("ForUpdate", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("TryStatementRest", rightCreator("{"));
		BNFRule.addFirst("TryStatementRest", rightCreator("("));
		BNFRule.addFirst("TryStatementBlockRest", rightCreator("catch"));
		BNFRule.addFirst("TryStatementBlockRest", rightCreator("finally"));
		BNFRule.addFirst("CatchClause", rightCreator("catch"));
		BNFRule.addFirst("CatchType", new Identifier());
		BNFRule.addFirst("Finally", rightCreator("finally"));
		BNFRule.addFirst("ResourceSpecification", rightCreator("("));
		BNFRule.addFirst("Resources", rightCreator("final", "@"), new Identifier());
		BNFRule.addFirst("Resource", rightCreator("final", "@"));
		BNFRule.addFirst("Resource", new Identifier());

		// Expression
		BNFRule.addFirst("Expression", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("AssignmentOperator", rightCreator("="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("+="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("-="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("*="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("/="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("&="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("|="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("^="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("%="));
		BNFRule.addFirst("AssignmentOperator", rightCreator("<<="));
		BNFRule.addFirst("AssignmentOperator", rightCreator(">>="));
		BNFRule.addFirst("AssignmentOperator", rightCreator(">>>="));
		BNFRule.addFirst("ExpressionA", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("ExpressionARest", rightCreator("?"));
		BNFRule.addFirst("ExpressionB", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("ExpressionBRest", rightCreator("||", "&&", "|", "^", "&", "==", "!=", "<", ">", "<=", ">=", "<<", ">>", ">>>", "+", "-", "*", "/", "%"));
		BNFRule.addFirst("ExpressionBRest", rightCreator("instanceof"));
		BNFRule.addFirst("InfixOp", rightCreator("||"));
		BNFRule.addFirst("InfixOp", rightCreator("&&"));
		BNFRule.addFirst("InfixOp", rightCreator("|"));
		BNFRule.addFirst("InfixOp", rightCreator("^"));
		BNFRule.addFirst("InfixOp", rightCreator("&"));
		BNFRule.addFirst("InfixOp", rightCreator("=="));
		BNFRule.addFirst("InfixOp", rightCreator("!="));
		BNFRule.addFirst("InfixOp", rightCreator("<"));
		BNFRule.addFirst("InfixOp", rightCreator(">"));
		BNFRule.addFirst("InfixOp", rightCreator("<="));
		BNFRule.addFirst("InfixOp", rightCreator(">="));
		BNFRule.addFirst("InfixOp", rightCreator("<<"));
		BNFRule.addFirst("InfixOp", rightCreator(">>"));
		BNFRule.addFirst("InfixOp", rightCreator(">>>"));
		BNFRule.addFirst("InfixOp", rightCreator("+"));
		BNFRule.addFirst("InfixOp", rightCreator("-"));
		BNFRule.addFirst("InfixOp", rightCreator("*"));
		BNFRule.addFirst("InfixOp", rightCreator("/"));
		BNFRule.addFirst("InfixOp", rightCreator("%"));
		BNFRule.addFirst("ExpressionC", rightCreator("++", "--", "!", "~", "+", "-"));
		BNFRule.addFirst("ExpressionC", rightCreator("("));
		BNFRule.addFirst("ExpressionC", rightCreator("this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("ExpressionCRest", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short"), new Identifier());
		BNFRule.addFirst("ExpressionCRest", rightCreator("++", "--", "!", "~", "+", "-", "("), new Literal());
		BNFRule.addFirst("PrefixOp", rightCreator("++"));
		BNFRule.addFirst("PrefixOp", rightCreator("--"));
		BNFRule.addFirst("PrefixOp", rightCreator("!"));
		BNFRule.addFirst("PrefixOp", rightCreator("~"));
		BNFRule.addFirst("PrefixOp", rightCreator("+"));
		BNFRule.addFirst("PrefixOp", rightCreator("-"));
		BNFRule.addFirst("PostfixOp", rightCreator("++"));
		BNFRule.addFirst("PostfixOp", rightCreator("--"));
		BNFRule.addFirst("Primary", rightCreator("this"));
		BNFRule.addFirst("Primary", rightCreator("super"));
		BNFRule.addFirst("Primary", rightCreator("new"));
		BNFRule.addFirst("Primary", rightCreator("<"));
		BNFRule.addFirst("Primary", rightCreator("boolean", "byte", "char", "double", "float", "int", "long", "short"));
		BNFRule.addFirst("Primary", rightCreator("void"));
		BNFRule.addFirst("Primary", new Identifier());
		BNFRule.addFirst("Primary", new Literal());
		BNFRule.addFirst("PrimaryB", rightCreator("super"), new Identifier());
		BNFRule.addFirst("PrimaryB", rightCreator("this"));
		BNFRule.addFirst("Literal", new Literal());
		BNFRule.addFirst("Arguments", rightCreator("("));
		BNFRule.addFirst("ArgumentsA", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("SuperSuffix", rightCreator("("));
		BNFRule.addFirst("SuperSuffix", rightCreator("."));
		BNFRule.addFirst("ExplicitGenericInvocationSuffix", rightCreator("super"));
		BNFRule.addFirst("ExplicitGenericInvocationSuffix", new Identifier());
		BNFRule.addFirst("Creator", rightCreator("<"));
		BNFRule.addFirst("Creator", new Identifier());
		BNFRule.addFirst("CreatorA", rightCreator("("));
		BNFRule.addFirst("CreatorA", rightCreator("["));
		BNFRule.addFirst("CreatedName", new Identifier());
		BNFRule.addFirst("ClassCreatorRest", rightCreator("("));
		BNFRule.addFirst("ClassBody", rightCreator("{"));
		BNFRule.addFirst("ArrayCreatorRest", rightCreator("["));
		BNFRule.addFirst("ArrayCreatorRestA", rightCreator("]"));
		BNFRule.addFirst("ArrayCreatorRestA", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("IdentifierSuffix", rightCreator("["));
		BNFRule.addFirst("IdentifierSuffix", rightCreator("("));
		BNFRule.addFirst("IdentifierSuffix", rightCreator("."));
		BNFRule.addFirst("IdentifierSuffixA", rightCreator("[]"));
		BNFRule.addFirst("IdentifierSuffixA", rightCreator("."));
		BNFRule.addFirst("IdentifierSuffixA", rightCreator("++", "--", "!", "~", "+", "-", "(", "this", "super", "new", "<", "boolean", "byte", "char", "double", "float", "int", "long", "short", "void"), new Identifier(), new Literal());
		BNFRule.addFirst("IdentifierSuffixB", rightCreator("class"));
		BNFRule.addFirst("IdentifierSuffixB", rightCreator("<"));
		BNFRule.addFirst("IdentifierSuffixB", rightCreator("this"));
		BNFRule.addFirst("IdentifierSuffixB", rightCreator("super"));
		BNFRule.addFirst("IdentifierSuffixB", rightCreator("new"));
		BNFRule.addFirst("ExplicitGenericInvocation", rightCreator("<"));
		BNFRule.addFirst("InnerCreator", new Identifier());
		BNFRule.addFirst("Selector", rightCreator("."));
		BNFRule.addFirst("Selector", rightCreator("["));
		BNFRule.addFirst("SelectorA", rightCreator("<"));
		BNFRule.addFirst("SelectorA", rightCreator("this"));
		BNFRule.addFirst("SelectorA", rightCreator("super"));
		BNFRule.addFirst("SelectorA", rightCreator("new"));
		BNFRule.addFirst("NonWildcardTypeArguments", rightCreator("<"));
		BNFRule.addFirst("TypeArgumentsOrDiamond", rightCreator("<>"));
		BNFRule.addFirst("TypeArgumentsOrDiamond", rightCreator("<"));
    }

	private static void prepareRuleBlock() throws ParseException {
		BNFRule.add("Block", rightCreator("\\{ {<BlockStatement>} \\}"));
		BNFRule.add("BlockStatement", rightCreator("<ClassOrInterfaceDeclaration>",
			"<Statement>",
			"<LocalVariableDeclarationStatement>"));
		BNFRule.add("LocalVariableDeclarationStatement", rightCreator("<VariableModifier> {<VariableModifier>} <Type> <VariableDeclarator> {, <VariableDeclarator>} ;",
			"<Type> <VariableDeclarator> {, <VariableDeclarator>} ;"));
		BNFRule.add("VariableInitializer", rightCreator("<ArrayInitializer>",
			"<Expression>"));
		BNFRule.add("Statement", rightCreator("<Block>",
			";",
			"if ( <Expression> ) <Statement> [else <Statement>]",
			"assert <Expression> [: <Expression>] ;",
			"switch ( <Expression> ) \\{ {<SwitchBlockStatementGroup>} \\}",
			"while ( <Expression> ) <Statement>",
			"do <Statement> while ( <Expression> ) ;",
			"for ( <ForControl> ) <Statement>",
			"break [<Identifier>] ;",
			"continue [<Identifier>] ;",
			"return [<Expression>] ;",
			"throw <Expression> ;",
			"synchronized ( <Expression> ) <Block>",
			"try <TryStatementRest>",
			"<Expression> ;"));
		BNFRule.add("SwitchBlockStatementGroup", rightCreator("<SwitchLabel> {<SwitchLabel>} {<BlockStatement>}"));
		BNFRule.add("SwitchLabel", rightCreator("case <SwitchLabelCase> :",
			"default :"));
		BNFRule.add("SwitchLabelCase", rightCreator("<Expression>",
			"<Identifier>"));
		BNFRule.add("ForControl", rightCreator("; [<Expression>] ; [<ForUpdate>]",
			"<ForInit> ; [<Expression>] ; [<ForUpdate>]",
			"<ForVarControl>"));
		BNFRule.add("ForVarControl", rightCreator("<VariableModifier> {<VariableModifier>} <Type> <VariableDeclaratorId> <ForVarControlRest>",
			"<Type> <VariableDeclaratorId> <ForVarControlRest>"));
		BNFRule.add("ForVarControlRest", rightCreator("; [<Expression>] ; [<ForUpdate>]",
			"<ForVariableDeclaratorsRest> ; [<Expression>] ; [<ForUpdate>]",
			": <Expression>"));
		BNFRule.add("ForVariableDeclaratorsRest", rightCreator("= <VariableInitializer> {, <VariableDeclarator>}",
			", <VariableDeclarator> {, <VariableDeclarator>}"));
		BNFRule.add("ForInit", rightCreator("<Expression> {, <Expression>}"));
		BNFRule.add("ForUpdate", rightCreator("<Expression> {, <Expression>}"));
		BNFRule.add("TryStatementRest", rightCreator("<Block> <TryStatementBlockRest>",
			"<ResourceSpecification> <Block> {<CatchClause>} [<Finally>]"));
		BNFRule.add("TryStatementBlockRest", rightCreator("<CatchClause> {<CatchClause>} [<Finally>]",
			"<Finally>"));
		BNFRule.add("CatchClause", rightCreator("catch ( {<VariableModifier>} <CatchType> <Identifier> ) <Block>"));
		BNFRule.add("CatchType", rightCreator("<QualifiedIdentifier> {| <QualifiedIdentifier>}"));
		BNFRule.add("Finally", rightCreator("finally <Block>"));
		BNFRule.add("ResourceSpecification", rightCreator("( <Resources> [;] )"));
		BNFRule.add("Resources", rightCreator("<Resource> {; <Resource>}"));
		BNFRule.add("Resource", rightCreator("<VariableModifier> {<VariableModifier>} <ReferenceType> <VariableDeclaratorId> = <Expression>",
			"<ReferenceType> <VariableDeclaratorId> = <Expression>"));
	}

	private static void prepareRuleExpression() throws ParseException {
		BNFRule.add("Expression", rightCreator("<ExpressionA> [<AssignmentOperator> <ExpressionA>]"));
		BNFRule.add("AssignmentOperator", rightCreator("=", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "\\<<=", "\\>>=", "\\>>>="));
		BNFRule.add("ExpressionA", rightCreator("<ExpressionB> [<ExpressionARest>]"));
		BNFRule.add("ExpressionARest", rightCreator("? <Expression> : <ExpressionA>"));
		BNFRule.add("ExpressionB", rightCreator("<ExpressionC> [<ExpressionBRest>]"));
		BNFRule.add("ExpressionBRest", rightCreator("<InfixOp> <ExpressionC> {<InfixOp> <ExpressionC>}",
			"instanceof <Type>"));
		BNFRule.add("InfixOp", rightCreator("||", "&&", "|", "^", "&", "==", "!=", "\\<", "\\>", "\\<=", "\\>=", "\\<<", "\\>>", "\\>>>", "+", "-", "*", "/", "%"));
		BNFRule.add("ExpressionC", rightCreator("<PrefixOp> <ExpressionC>",
			"( <ExpressionCRest> ) <ExpressionC>",
			"<Primary> {<Selector>} {<PostfixOp>}"));
		BNFRule.add("ExpressionCRest", rightCreator("<Type> ) <ExpressionC>", "<Expression> )"));
		BNFRule.add("PrefixOp", rightCreator("++", "--", "!", "~", "+", "-"));
		BNFRule.add("PostfixOp", rightCreator("++", "--"));
		BNFRule.add("Primary", rightCreator("this [<Arguments>]",
			"super <SuperSuffix>",
			"new <Creator>",
			"<NonWildcardTypeArguments> ( <PrimaryB> )",
			"<BasicType> {\\[]} . class",
			"void . class",
			"<Identifier> {. <Identifier>} [<IdentifierSuffix>]",
			"<Literal>"));
		BNFRule.add("PrimaryB", rightCreator("<ExplicitGenericInvocationSuffix>",
			"this <Arguments>"));
		BNFRule.add("Literal", new Literal());
		BNFRule.add("Arguments", rightCreator("( [<ArgumentsA>] )"));
		BNFRule.add("ArgumentsA", rightCreator("<Expression> {, <Expression>}"));
		BNFRule.add("SuperSuffix", rightCreator("<Arguments>",
			". <Identifier> [<Arguments>]"));
		BNFRule.add("ExplicitGenericInvocationSuffix", rightCreator("super <SuperSuffix>",
			"<Identifier> <Arguments>"));
		BNFRule.add("Creator", rightCreator("<NonWildcardTypeArguments> <CreatedName> <ClassCreatorRest>", "<CreatedName> <CreatorA>"));
		BNFRule.add("CreatorA", rightCreator("<ClassCreatorRest>",
			"<ArrayCreatorRest>"));
		BNFRule.add("CreatedName", rightCreator("<Identifier> [<TypeArgumentsOrDiamond>] {. <Identifier> [<TypeArgumentsOrDiamond>]}"));
		BNFRule.add("ClassCreatorRest", rightCreator("<Arguments> [<ClassBody>]"));
		BNFRule.add("ClassBody", rightCreator("\\{ {<ClassBodyDeclaration>} \\}"));
		BNFRule.add("ArrayCreatorRest", rightCreator("\\[ <ArrayCreatorRestA>"));
		BNFRule.add("ArrayCreatorRestA", rightCreator("\\] {\\[]} <ArrayInitializer>",
			"<Expression> \\] {\\[ <Expression> \\]} {\\[]}"));
		BNFRule.add("IdentifierSuffix", rightCreator("\\[ <IdentifierSuffixA> \\]",
			"<Arguments>",
			". <IdentifierSuffixB>"));
		BNFRule.add("IdentifierSuffixA", rightCreator("\\[] {\\[]} . class",
			". class",
			"<Expression>"));
		BNFRule.add("IdentifierSuffixB", rightCreator("class",
			"<ExplicitGenericInvocation>",
			"this",
			"super <Arguments>",
			"new [<NonWildcardTypeArguments>] <InnerCreator>"));
		BNFRule.add("ExplicitGenericInvocation", rightCreator("<NonWildcardTypeArguments> <ExplicitGenericInvocationSuffix>"));
		BNFRule.add("InnerCreator", rightCreator("<Identifier> [NonWildcardTypeArgumentsOrDiamond] <ClassCreatorRest>"));
		BNFRule.add("Selector", rightCreator(". <SelectorA>",
			"\\[ <Expression> \\]"));
		BNFRule.add("SelectorA", rightCreator("<ExplicitGenericInvocation>",
			"this",
			"super <SuperSuffix>",
			"new [<NonWildcardTypeArguments>] <InnerCreator>"));
		BNFRule.add("NonWildcardTypeArguments", rightCreator("\\< <TypeList> \\>"));
		BNFRule.add("TypeArgumentsOrDiamond", rightCreator("\\<>", "<TypeArguments>"));
	}

    public static String[] rightCreator(String... right) {
        return right;
    }

    public static void runProgram() throws ParseException {
        JavaEngine.parser.readNextToken();
        BNFRule rule = BNFRule.get("Program");

        try {
            rule.parse();
        } catch (RuleNotMatchException e) {
            JavaEngine.parser.markError(e);
        }
    }
}
