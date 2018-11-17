package id.ac.itb.if5020.t2018.components.specialrules;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class Identifier extends NonTerminalSymbol implements SpecialRule {

    public Identifier() {
        super("<#IDENTIFIER#>");
    }

    @Override
    public void match(BNFRule currentRule) throws ParseException {
        String token = JavaEngine.parser.getCurrentToken();
        if (matching(token.charAt(0)) && token.matches("^[A-Za-z_][A-Za-z0-9_]*")) {
            JavaEngine.parser.readNextToken();
        } else {
            throw new RuleNotMatchException("Token is not Identifier", currentRule);
        }
    }

    @Override
    public boolean matching(char _char) {
        return String.valueOf(_char).matches("^[a-zA-Z_]$");
    }
}