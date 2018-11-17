package id.ac.itb.if5020.t2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.specialrules.Identifier;
import id.ac.itb.if5020.t2018.helpers.TextFileParser;

public class TestParsingRule {

    @Before
    public void before() {
        JavaEngine.parser = null;
        BNFRule.clear();
    }

    @Test
    public void testParsingIdentifier() throws IOException, ParseException {
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("t")));
        parse("Identifier");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("_t")));
        parse("Identifier");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("t_")));
        parse("Identifier");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("_t_")));
        parse("Identifier");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("t0123")));
        parse("Identifier");
    }

    @Test
    public void testParsingImportDeclaration() throws IOException, ParseException {
        BNFRule.add("ImportDeclaration", JavaEngine.rightCreator("import [static] <Identifier> <ImportDeclarationEnd>"));
        BNFRule.add("ImportDeclarationEnd", JavaEngine.rightCreator(". <ImportDeclarationStarEnd>", ";"));
        BNFRule.add("ImportDeclarationStarEnd", JavaEngine.rightCreator("* ;", "<Identifier> <ImportDeclarationEnd>"));

        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("import id.ac._it_b.t2015;")));
        parse("ImportDeclaration");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("import id.ac._it_b.t2015.*;")));
        parse("ImportDeclaration");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("import static id.ac._it_b.t2015.*;")));
        parse("ImportDeclaration");
    }

    @Test
    public void testParsingPackage() throws IOException, ParseException {
        BNFRule.add("PackageDeclaration", JavaEngine.rightCreator("{<Annotation>} package"));

        // ANNOTATION
        BNFRule.add("Annotation", JavaEngine.rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", JavaEngine.rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement",
                JavaEngine.rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", JavaEngine.rightCreator("= <AnnotationElementValue>"));
        BNFRule.add("AnnotationElementValue",
                JavaEngine.rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues",
                JavaEngine.rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("QualifiedIdentifier", JavaEngine.rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("PackageDeclaration", JavaEngine.rightCreator("@", "package"));
        BNFRule.addFirst("Annotation", JavaEngine.rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", JavaEngine.rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", JavaEngine.rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", JavaEngine.rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", JavaEngine.rightCreator("{"));

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("@s package")));
        parse("PackageDeclaration");
    }

    @Test
    public void testParsing4() throws IOException, ParseException {
        // PACKAGEDECLARATION
        BNFRule.add("PackageDeclaration", JavaEngine.rightCreator("{<Annotation>} package <QualifiedIdentifier> ;"));

        // ANNOTATION
        BNFRule.add("Annotation", JavaEngine.rightCreator("@ <QualifiedIdentifier> [<AnnotationRest>]"));
        BNFRule.add("AnnotationRest", JavaEngine.rightCreator("( [<AnnotationElement>] )"));
        BNFRule.add("AnnotationElement", JavaEngine.rightCreator("<AnnotationElementValue>", "<Identifier> [<AnnotationElementRest>]"));
        BNFRule.add("AnnotationElementRest", JavaEngine.rightCreator("= <AnnotationElementValue>"));
        BNFRule.add("AnnotationElementValue", JavaEngine.rightCreator("<Annotation>", "\\{ [<AnnotationElementValues>] [,] \\}"));
        BNFRule.add("AnnotationElementValues", JavaEngine.rightCreator("<AnnotationElementValue> {, <AnnotationElementValue>}"));

        BNFRule.add("QualifiedIdentifier", JavaEngine.rightCreator("<Identifier> {. <Identifier>}"));
        BNFRule.add("Identifier", new Identifier());

        BNFRule.addFirst("PackageDeclaration", JavaEngine.rightCreator("@", "package"));
        BNFRule.addFirst("Annotation", JavaEngine.rightCreator("@"));
        BNFRule.addFirst("AnnotationRest", JavaEngine.rightCreator("("));
        BNFRule.addFirst("AnnotationElementRest", JavaEngine.rightCreator("="));
        BNFRule.addFirst("AnnotationElementValue", JavaEngine.rightCreator("@"));
        BNFRule.addFirst("AnnotationElementValue", JavaEngine.rightCreator("{"));

        BNFRule.addFirst("Identifier", new Identifier());

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("@ SuchAnnotation package id.ac._itb.if5020.t2018;")));
        parse("PackageDeclaration");

        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("@ package id.ac._itb.if5020.t2018;")));
        parseErrorExpected("PackageDeclaration", "Terminal 'package' expected");
    }

    private void parse(String startSymbol) throws IOException, ParseException {
        try {
            JavaEngine.parser.readNextToken();
            BNFRule.get(startSymbol).parse();
        } catch (RuleNotMatchException e) {
            Assert.fail("No exception should no be thrown");
        }
        Assert.assertTrue("Parsing should be until end of file", JavaEngine.parser.isEndOfFile());
    }

    private void parseErrorExpected(String startSymbol, String exceptionMessageExpected) throws IOException, ParseException {
        try {
            JavaEngine.parser.readNextToken();
            BNFRule.get(startSymbol).parse();
            Assert.assertFalse("Parsing should be not achieved to end of file", JavaEngine.parser.isEndOfFile());
        } catch (RuleNotMatchException e) {
            JavaEngine.parser.markError(e);
            Assert.assertEquals(e.getMessage(), exceptionMessageExpected);
        }
    }
}