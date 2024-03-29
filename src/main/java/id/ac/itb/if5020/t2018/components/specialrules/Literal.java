package id.ac.itb.if5020.t2018.components.specialrules;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.BNFRule;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class Literal extends NonTerminalSymbol implements SpecialRule {

    public Literal() {
        super("<#LITERAL#>");
    }

    @Override
    public void match(BNFRule currentRule) throws ParseException {
        String token = JavaEngine.parser.getCurrentToken();
        if (matching(token.charAt(0)) && (token.matches("^0[lL]?$")
			|| token.matches("^[1-9]([0-9_]*[0-9])?[lL]?$") 
			|| token.matches("^0[xX][0-9A-Fa-f]([0-9A-Fa-f_]*[0-9A-Fa-f])?[lL]?$")
			|| token.matches("^0[0-7]([0-7_]*[0-7])?[lL]?$")
			|| token.matches("^0[bB][01]([01_]*[01])?[lL]?$")
			|| token.matches("^[0-9]([0-9_]*[0-9])?\\.([0-9]([0-9_]*[0-9])?)?([eE][+-]?[0-9]([0-9_]*[0-9])?)?[fFdD]?$")
			|| token.matches("^\\.[0-9]([0-9_]*[0-9])?([eE][+-]?[0-9]([0-9_]*[0-9])?)?[fFdD]?$")
			|| token.matches("^[0-9]([0-9_]*[0-9])?[eE][+-]?[0-9]([0-9_]*[0-9])?[fFdD]?$")
			|| token.matches("^[0-9]([0-9_]*[0-9])?[fFdD]$")
			|| token.matches("^0[xX][0-9A-Fa-f]([0-9A-Fa-f_]*[0-9A-Fa-f])?[\\.]?[pP][+-]?[0-9]([0-9_]*[0-9])?[fFdD]?$")
			|| token.matches("^0[xX]([0-9A-Fa-f]([0-9A-Fa-f_]*[0-9A-Fa-f])?)?\\.[0-9A-Fa-f]([0-9A-Fa-f_]*[0-9A-Fa-f])?[pP][+-]?[0-9]([0-9_]*[0-9])?[fFdD]?$")
			|| token.matches("^'.{1,2}'$")
			|| token.matches("^\".*\"$")
			|| token.matches("^(true|false)$")
			|| token.matches("^null$"))) {
            JavaEngine.parser.readNextToken();
        } else {
            throw new RuleNotMatchException("Token is not a number literal", currentRule);
        }
    }

    @Override
    public boolean matching(char _char) {
        return String.valueOf(_char).matches("^[0-9\\.'\"tfn]$");
    }

    @Override
    public String getRuleName() {
        return symbol;
    }
}