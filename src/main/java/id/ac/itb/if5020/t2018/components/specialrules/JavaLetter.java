package id.ac.itb.if5020.t2018.components.specialrules;

import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class JavaLetter extends NonTerminalSymbol implements SpecialRule {

    public JavaLetter() {
        super("<#JAVALETTER#>");
    }

    @Override
    public void match() throws ParseException {
        if (matching(JavaEngine.parser.getCurrentTokenChar())) {
            JavaEngine.parser.readCurrentTokenChar();
        } else {
            throw new RuleNotMatchException("Character is not Java letter");
        }
    }

    @Override
    public boolean matching(char _char) {
        return String.valueOf(_char).matches("^[a-zA-Z]$");
    }
}