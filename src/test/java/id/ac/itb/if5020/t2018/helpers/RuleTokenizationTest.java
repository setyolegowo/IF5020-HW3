
package id.ac.itb.if5020.t2018.helpers;

import org.junit.Assert;
import org.junit.Test;

public class RuleTokenizationTest {

    @Test
    public void testExecutable() throws Exception {
        RuleTokenization tokenization = new RuleTokenization("terminalsymbol");
        Assert.assertEquals("expected", tokenization.readNextToken(), "terminalsymbol");
    }
}