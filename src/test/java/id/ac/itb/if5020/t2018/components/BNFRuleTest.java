package id.ac.itb.if5020.t2018.components;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class BNFRuleTest {

    @Test
    public void testInsert() throws ParseException {
        BNFRule.add("program", rc("[<PackageDeclaration>] [<ImportDeclaration>] [<Declarations>]"));
    }

    @Test
    public void testAccess() throws ParseException {
        BNFRule.add("program", rc("[<PackageDeclaration>] [<ImportDeclaration>] [<Declarations>]"));
        BNFRule rule = BNFRule.get("program");
        Assert.assertTrue(BNFRule.class.isInstance(rule));
    }

    private static String[] rc(String... str) {
        return str;
    }
}