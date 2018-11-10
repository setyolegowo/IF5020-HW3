
package id.ac.itb.if5020.t2018.components;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;
import id.ac.itb.if5020.t2018.components.symbols.OptionalSymbol;
import id.ac.itb.if5020.t2018.components.symbols.RepetitionSymbol;
import id.ac.itb.if5020.t2018.components.symbols.TerminalSymbol;

public class BNFSingleRuleTest {

    @Test
    public void testSingleTerminal() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("if");
            Assert.assertEquals(1, singleRule.tokenRule.size());
            Assert.assertTrue(TerminalSymbol.class.isInstance(singleRule.tokenRule.get(0)));

            singleRule = new BNFSingleRule("\\{");
            Assert.assertEquals(1, singleRule.tokenRule.size());
            Assert.assertTrue(TerminalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testSingleNonTerminal() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("<PackageDeclaration>");
            Assert.assertEquals(1, singleRule.tokenRule.size());
            Assert.assertTrue(NonTerminalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testSingleRepetitionTerminal() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("{<PackageDeclaration>}");
            Assert.assertEquals(1, singleRule.tokenRule.size());
            Assert.assertTrue(RepetitionSymbol.class.isInstance(singleRule.tokenRule.get(0)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testSingleOptionalTerminal() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("[<PackageDeclaration>]");
            Assert.assertEquals(1, singleRule.tokenRule.size());
            Assert.assertTrue(OptionalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testTerminalAndNonTerminal() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("package <Identifier>");
            Assert.assertEquals(2, singleRule.tokenRule.size());
            Assert.assertTrue(TerminalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
            Assert.assertTrue(NonTerminalSymbol.class.isInstance(singleRule.tokenRule.get(1)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testTerminalAndRepetition() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("package {<Identifier>}");
            Assert.assertEquals(2, singleRule.tokenRule.size());
            Assert.assertTrue(TerminalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
            Assert.assertTrue(RepetitionSymbol.class.isInstance(singleRule.tokenRule.get(1)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testTerminalAndOptional() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("package [<Identifier>]");
            Assert.assertEquals(2, singleRule.tokenRule.size());
            Assert.assertTrue(TerminalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
            Assert.assertTrue(OptionalSymbol.class.isInstance(singleRule.tokenRule.get(1)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testOptionalAndRepetition() {
        try {
            BNFSingleRule singleRule = new BNFSingleRule("[<Identifier>] {<FullyQualified>}");
            Assert.assertEquals(2, singleRule.tokenRule.size());
            Assert.assertTrue(OptionalSymbol.class.isInstance(singleRule.tokenRule.get(0)));
            Assert.assertTrue(RepetitionSymbol.class.isInstance(singleRule.tokenRule.get(1)));
        } catch (ParseException exc) {
            Assert.fail("No exception should be thrown");
        }
    }
}