package id.ac.itb.if5020.t2018.components;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetter;
import id.ac.itb.if5020.t2018.components.specialrules.JavaLetterOrDigit;

public class BNFRuleTest {

    @Before
    public void before() {
        BNFRule.clear();
    }

    @Test
    public void testInsert() throws ParseException {
        BNFRule.add("program", JavaEngine.rightCreator("[<PackageDeclaration>] [<ImportDeclaration>] [<Declarations>]"));
    }

    @Test
    public void testAccess() throws ParseException {
        BNFRule.add("program", JavaEngine.rightCreator("[<PackageDeclaration>] [<ImportDeclaration>] [<Declarations>]"));
        BNFRule rule = BNFRule.get("program");
        Assert.assertTrue(BNFRule.class.isInstance(rule));
    }

    @Test
    public void testRunnableJavaEngine() throws ParseException {
        JavaEngine.prepareRules();
    }

    @Test
    public void testSimpleParsing() throws ParseException {
        BNFRule.add("program", JavaEngine.rightCreator("[<PackageDeclaration>]"));
        BNFRule.add("PackageDeclaration", JavaEngine.rightCreator("package <QualifiedIdentifier> ;"));
        BNFRule.add("QualifiedIdentifier", JavaEngine.rightCreator("<Identifier> . <Identifier>"));
        BNFRule.add("Identifier", JavaEngine.rightCreator("<JavaLetter> {<JavaLetterOrDigit>}"));
        BNFRule.add("JavaLetter", new JavaLetter());
        BNFRule.add("JavaLetterOrDigit", new JavaLetterOrDigit());
    }
}