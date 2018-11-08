package id.ac.itb.if5020.t2018.components.specialrules;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class JavaLetter extends NonTerminalSymbol implements SpecialRule {

    public JavaLetter() {
        super("<#JAVALETTER#>");
    }

    @Override
    public void match() {
        String curchar = String.valueOf(JavaEngine.parser.getCurrentTokenChar());
        if (curchar.matches("^[a-zA-Z]$")) {
            JavaEngine.parser.readCurrentTokenChar();
        } else {
            throw new RuleNotMatchException("Character is not Java letter");
        }
    }
}