package id.ac.itb.if5020.t2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetterOrDigit;
import id.ac.itb.if5020.t2018.helpers.TextFileParser;

public class TestParsingRule {

    @Before
    public void before() {
        JavaEngine.parser = null;
        BNFRule.clear();
    }

    @Test
    public void testParsing1() throws IOException, ParseException {
        JavaEngine.parser = new TextFileParser(new BufferedReader(new StringReader("(F)")));

        BNFRule.add("Start", JavaEngine.rightCreator("( <JavaLetterOrDigit> )"));
        BNFRule.add("JavaLetterOrDigit", new JavaLetterOrDigit());

        parse("Start");
    }

    private void parse(String startSymbol) throws IOException, ParseException {
        JavaEngine.parser.readNextToken();
        BNFRule.get(startSymbol).parse();
        Assert.assertTrue("Parsing should be until end of file", JavaEngine.parser.isEndOfFile());
    }
}