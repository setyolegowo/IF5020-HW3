
package id.ac.itb.if5020.t2018.helpers;

import java.text.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class RuleTokenizationTest {

    @Test
    public void testExecutable() throws Exception {
        RuleTokenization tokenization = new RuleTokenization("terminalsymbol");
        Assert.assertEquals("test executable", tokenization.readNextToken(), "terminalsymbol");
    }

    @Test
    public void testTokenize() throws Exception {
        RuleTokenization tokenization = new RuleTokenization("[terminalsymbol <NonTerminal>]");
        Assert.assertEquals("has next", tokenization.hasNext(), true);
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "[terminalsymbol <NonTerminal>]");
        Assert.assertEquals("finish", tokenization.hasNext(), false);

        tokenization = new RuleTokenization("[terminalsymbol <NonTerminal>] {<NonTerminal>}");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "[terminalsymbol <NonTerminal>]");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "{<NonTerminal>}");
        Assert.assertEquals("finish", tokenization.hasNext(), false);

        tokenization = new RuleTokenization("{terminalsymbol <NonTerminal>} {<NonTerminal>}");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "{terminalsymbol <NonTerminal>}");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "{<NonTerminal>}");
        Assert.assertEquals("finish", tokenization.hasNext(), false);

        tokenization = new RuleTokenization("terminalsymbol <NonTerminal> [<NonTerminal> terminal]");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "terminalsymbol");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "<NonTerminal>");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "[<NonTerminal> terminal]");
        Assert.assertEquals("finish", tokenization.hasNext(), false);

        tokenization = new RuleTokenization("\\{ \\}");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "\\{");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "\\}");
        Assert.assertEquals("finish", tokenization.hasNext(), false);

        tokenization = new RuleTokenization("\\[]");
        Assert.assertEquals("test tokenize", tokenization.readNextToken(), "\\[]");
        Assert.assertEquals("finish", tokenization.hasNext(), false);
    }

    @Test(expected = ParseException.class)
    public void testExceptionEmpty() throws ParseException {
        RuleTokenization tokenization = new RuleTokenization("");
        tokenization.readNextToken();
    }

    @Test(expected = ParseException.class)
    public void testExceptionEmpty2() throws ParseException {
        RuleTokenization tokenization = new RuleTokenization(" ");
        tokenization.readNextToken();
    }

    @Test(expected = ParseException.class)
    public void testException() throws ParseException {
        RuleTokenization tokenization = new RuleTokenization("[");
        tokenization.readNextToken();
    }

    @Test(expected = ParseException.class)
    public void testException2() throws ParseException {
        RuleTokenization tokenization = new RuleTokenization("[] {");
        tokenization.readNextToken();
        tokenization.readNextToken();
    }
}