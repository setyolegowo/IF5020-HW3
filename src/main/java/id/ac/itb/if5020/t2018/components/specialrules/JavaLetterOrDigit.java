package id.ac.itb.if5020.t2018.components.specialrules;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class JavaLetterOrDigit extends NonTerminalSymbol implements SpecialRule {

    public JavaLetterOrDigit() {
        super("<#JAVALETTERORDIGIT#>");
    }

    @Override
    public void match(BNFRule currentRule) throws ParseException {
        if (matching(JavaEngine.parser.getCurrentTokenChar())) {
            JavaEngine.parser.readCurrentTokenChar();
        } else {
            throw new RuleNotMatchException("Character is not Java letter or digit", currentRule);
        }
    }

    @Override
    public boolean matching(char _char) {
        return String.valueOf(_char).matches("^[a-zA-Z0-9]$");
    }

    @Override
    public String getRuleName() {
        return symbol;
    }
}