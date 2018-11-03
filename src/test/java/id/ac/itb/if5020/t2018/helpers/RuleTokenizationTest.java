
package id.ac.itb.if5020.t2018.helpers;

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
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "[terminalsymbol <NonTerminal>]");
        Assert.assertEquals("finish", tokenization.hasNext(), false);

        tokenization = new RuleTokenization("[terminalsymbol <NonTerminal>] {<NonTerminal>}");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "[terminalsymbol <NonTerminal>]");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "{<NonTerminal>}");

        tokenization = new RuleTokenization("{terminalsymbol <NonTerminal>} {<NonTerminal>}");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "{terminalsymbol <NonTerminal>}");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "{<NonTerminal>}");

        tokenization = new RuleTokenization("terminalsymbol <NonTerminal> [<NonTerminal> terminal]");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "terminalsymbol");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "<NonTerminal>");
        Assert.assertEquals("test tokneize", tokenization.readNextToken(), "[<NonTerminal> terminal]");
    }
}