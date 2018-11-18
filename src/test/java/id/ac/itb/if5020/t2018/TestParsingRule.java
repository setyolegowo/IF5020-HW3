package id.ac.itb.if5020.t2018;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.specialrules.Identifier;
import id.ac.itb.if5020.t2018.components.specialrules.Literal;
import id.ac.itb.if5020.t2018.helpers.TextStringParser;

import static id.ac.itb.if5020.t2018.JavaEngine.rightCreator;

public class TestParsingRule {

    @Before
    public void before() {
        JavaEngine.throwNonLL1 = true;
        JavaEngine.parser = null;
        BNFRule.clear();
    }

	@Test
	public void testLiteral() throws IOException, ParseException {
		BNFRule.add("Literal", new Literal());
		BNFRule.addFirst("Literal", new Literal());

		JavaEngine.parser = new TextStringParser("0");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("2");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("1996");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("19_96");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0xDadaCafe");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0x00_FF__00_FF");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0372");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0177_7777_7777");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0b0111_1111_1111_1111_1111_1111_1111_1111");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0l");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0777L");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0x100000000L");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("2_147_483_648L");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("0xC0B0L");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("'a'");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("\"test\"");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("false");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("null");
        parse("Literal");

		JavaEngine.parser = new TextStringParser("'\\n'");
        parse("Literal");
	}

    @Test
    public void testParsingIdentifier() throws IOException, ParseException {
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextStringParser("t");
        parse("Identifier");

        JavaEngine.parser = new TextStringParser("_t");
        parse("Identifier");

        JavaEngine.parser = new TextStringParser("t_");
        parse("Identifier");

        JavaEngine.parser = new TextStringParser("_t_");
        parse("Identifier");

        JavaEngine.parser = new TextStringParser("t0123");
        parse("Identifier");
    }

    @Test
    public void testParsingImportDeclaration() throws IOException, ParseException {
        BNFRule.add("ImportDeclaration", rightCreator("import [static] <Identifier> <ImportDeclarationEnd>"));
        BNFRule.add("ImportDeclarationEnd", rightCreator(". <ImportDeclarationStarEnd>", ";"));
        BNFRule.add("ImportDeclarationStarEnd", rightCreator("* ;", "<Identifier> <ImportDeclarationEnd>"));

        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextStringParser("import id.ac._it_b.t2015;");
        parse("ImportDeclaration");

        JavaEngine.parser = new TextStringParser("import id.ac._it_b.t2015.*;");
        parse("ImportDeclaration");

        JavaEngine.parser = new TextStringParser("import static id.ac._it_b.t2015.*;");
        parse("ImportDeclaration");
    }

    @Test
    public void testParsingPackage() throws IOException, ParseException {
        BNFRule.add("PackageDeclaration", rightCreator("{<Annotation>} package"));

        // ANNOTATION
        BNFRule.add("Annotation", rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement",
                rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", rightCreator("= <AnnotationElementValue>"));
        BNFRule.add("AnnotationElementValue",
                rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues",
                rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("PackageDeclaration", rightCreator("@", "package"));
        BNFRule.addFirst("Annotation", rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("{"));

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextStringParser("@s package");
        parse("PackageDeclaration");
    }

    @Test
    public void testParsing4() throws IOException, ParseException {
        // PACKAGEDECLARATION
        BNFRule.add("PackageDeclaration", rightCreator("{<Annotation>} package <QualifiedIdentifier> ;"));

        // ANNOTATION
        BNFRule.add("Annotation", rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement", rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", rightCreator("= <AnnotationElementValue>"));
        BNFRule.add("AnnotationElementValue", rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues", rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("PackageDeclaration", rightCreator("@", "package"));
        BNFRule.addFirst("Annotation", rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("{"));

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextStringParser("@ SuchAnnotation package id.ac._itb.if5020.t2018;");
        parse("PackageDeclaration");

        JavaEngine.parser = new TextStringParser("@ package id.ac._itb.if5020.t2018;");
        parseErrorExpected("PackageDeclaration", "Terminal 'package' expected");
    }

    @Test
    public void testParsing5() throws IOException, ParseException {
        BNFRule.add("EarlyAnnotation", rightCreator("<PackageDeclaration> {<ImportDeclaration>}"));

        // PACKAGEDECLARATION
        BNFRule.add("PackageDeclaration", rightCreator("package <QualifiedIdentifier> ;"));

        // IMPORTDECLARATION
        BNFRule.add("ImportDeclaration", rightCreator("import [static] <Identifier> <ImportDeclarationEnd>"));
        BNFRule.add("ImportDeclarationEnd", rightCreator(". <ImportDeclarationStarEnd>", ";"));
        BNFRule.add("ImportDeclarationStarEnd", rightCreator("* ;", "<Identifier> <ImportDeclarationEnd>"));

        // ANNOTATION
        BNFRule.add("Annotation", rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement",
                rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", rightCreator("= <AnnotationElementValue>"));
        BNFRule.add("AnnotationElementValue",
                rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues",
                rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("QualifiedIdentifier", rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("PackageDeclaration", rightCreator("package"));
        BNFRule.addFirst("ImportDeclaration", rightCreator("import"));
        BNFRule.addFirst("ImportDeclarationEnd", rightCreator("."));
        BNFRule.addFirst("ImportDeclarationEnd", rightCreator(";"));
        BNFRule.addFirst("ImportDeclarationStarEnd", rightCreator("*"));
        BNFRule.addFirst("ImportDeclarationStarEnd", new Identifier());
        BNFRule.addFirst("Annotation", rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", rightCreator("{"));

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextStringParser("package id.ac._itb.if5020.t2018;\nimport java.text.ParseException;");
        parse("EarlyAnnotation");

        JavaEngine.parser = new TextStringParser("package id.ac._itb.if5020.t2018;\n\nimport java.text.ParseException");
        parseErrorExpected("EarlyAnnotation", "Token is not match with rule ImportDeclarationEnd. Expected '.', ';', ");
    }

    @Test
    public void testActualRule() throws IOException, ParseException {
        JavaEngine.prepareRules();
        JavaEngine.prepareFirstList();

        JavaEngine.parser = new TextStringParser("package id.ac._itb.if5020.t2018;\nimport java.text.ParseException;\n");
        parse("Program");

        JavaEngine.parser = new TextStringParser(
            "@SuchAnnotation package id.ac._itb.if5020.t2018;\n" +
            "import java.text.ParseException;\n"
        );
        parse("Program");

        JavaEngine.parser = new TextStringParser(
            "@SuchAnnotation package id.ac._it_b.if5020.t2018;\n" +
            "import java.text.ParseException;\n" +
            "import static java.text.PARSE;\n" +
            "import java.text.*;\n" +
            "import static java.text.*;");
        parse("Program");

        JavaEngine.parser = new TextStringParser(
            "@SuchAnnotation public class MyClass {\n" +
            "}\n"
        );
        parse("Program");

        JavaEngine.parser = new TextStringParser("public void MyFunction();");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("protected char MyFunction();");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("protected Identity[] MyFunction();");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity[][] MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity<> MyFunction;");
        parseErrorExpected("ClassBodyDeclaration", "Token is not Identifier");

        JavaEngine.parser = new TextStringParser("private Identity<AnotherClass> MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity<?> MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity<? extends AnotherClass> MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity<? super AnotherClass> MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity<?>[] MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity[][] MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity[][] MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private boolean[][] MyFunction;");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private boolean<?> MyFunction;");
        parseErrorExpected("ClassBodyDeclaration", "Token is not match with rule MemberDeclarationRestAfterBasic. Expected <#IDENTIFIER#>, ");

        JavaEngine.parser = new TextStringParser("private MyFunction;");
        parseErrorExpected("ClassBodyDeclaration", "Token is not match with rule MemberDeclarationRest. Expected '<', '(', '[]', <#IDENTIFIER#>, ");

        JavaEngine.parser = new TextStringParser("private MyFunction() {}");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("private Identity MyFunction {");
        parseErrorExpected("ClassBodyDeclaration", "Token is not match with rule MethodOrFieldRest. Expected '(', '=', '[]', ';', ");

        JavaEngine.parser = new TextStringParser("protected Identity MyFunction(@Ann byte myvar);");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("void MyFunction();");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("static final MyFunction() {}");
        parse("ClassBodyDeclaration");

        JavaEngine.parser = new TextStringParser("static {}");
        parse("ClassBodyDeclaration");
    }

    private void parse(String startSymbol) throws IOException, ParseException {
        try {
            JavaEngine.parser.readNextToken();
            BNFRule rule = BNFRule.get(startSymbol);
            rule.parse();
        } catch (RuleNotMatchException e) {
            Assert.fail("No exception should no be thrown. Message: " + e.getMessage());
        }
        Assert.assertTrue("Parsing should be until end of file", JavaEngine.parser.isEndOfFile());
    }

    private void parseErrorExpected(String startSymbol, String exceptionMessageExpected) throws IOException, ParseException {
        try {
            JavaEngine.parser.readNextToken();
            BNFRule rule = BNFRule.get(startSymbol);
            rule.parse();
            Assert.assertFalse("Parsing should be not achieved to end of file", JavaEngine.parser.isEndOfFile());
        } catch (RuleNotMatchException e) {
            JavaEngine.parser.markError(e);
            Assert.assertEquals(e.getMessage(), exceptionMessageExpected);
        }
    }
}