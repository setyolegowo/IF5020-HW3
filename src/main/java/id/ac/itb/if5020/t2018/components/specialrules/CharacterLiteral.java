package id.ac.itb.if5020.t2018.components.specialrules;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class CharacterLiteral extends NonTerminalSymbol implements SpecialRule {

    public CharacterLiteral() {
        super("<#CHARACTERLITERAL#>");
    }

    @Override
    public void match(BNFRule currentRule) throws ParseException {
        String token = JavaEngine.parser.getCurrentToken();
        if (matching(token.charAt(0)) && token.matches("^'.{1,2}'$")) {
            JavaEngine.parser.readNextToken();
        } else {
            throw new RuleNotMatchException("Token is not a character literal", currentRule);
        }
    }

    @Override
    public boolean matching(char _char) {
        return String.valueOf(_char).matches("'");
    }
	
	@Override
    public String getRuleName() {
        return symbol;
    }
}