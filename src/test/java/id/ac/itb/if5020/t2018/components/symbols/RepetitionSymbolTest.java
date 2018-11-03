
package id.ac.itb.if5020.t2018.components.symbols;

import java.security.InvalidParameterException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class RepetitionSymbolTest {

    @Test
    public void testCreation() {
        try {
            new RepetitionSymbol("{<Identifier>}");
        } catch (ParseException e) {
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testEmpty() {
        try {
            new RepetitionSymbol("{}");
            Assert.fail("There must be throw ParseException");
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        } catch (InvalidParameterException e) {
            Assert.assertEquals("Rules inside must be not empty", e.getMessage());
        }
    }

    @Test
    public void testInvalidChar() {
        try {
            new RepetitionSymbol(".");
            Assert.fail("There must be throw ParseException");
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        } catch (InvalidParameterException e) {
            Assert.assertEquals("Symbol must start with '{' and end with '}'", e.getMessage());
        }

        try {
            new RepetitionSymbol("[]");
            Assert.fail("There must be throw ParseException");
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        } catch (InvalidParameterException e) {
            Assert.assertEquals("Symbol must start with '{' and end with '}'", e.getMessage());
        }

        try {
            new RepetitionSymbol("{]");
            Assert.fail("There must be throw ParseException");
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        } catch (InvalidParameterException e) {
            Assert.assertEquals("Symbol must start with '{' and end with '}'", e.getMessage());
        }

        try {
            new RepetitionSymbol("[}");
            Assert.fail("There must be throw ParseException");
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        } catch (InvalidParameterException e) {
            Assert.assertEquals("Symbol must start with '{' and end with '}'", e.getMessage());
        }
    }
}