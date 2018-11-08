package id.ac.itb.if5020.t2018.components.specialrules;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;
import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class JavaLetterOrDigit extends NonTerminalSymbol implements SpecialRule {

    public JavaLetterOrDigit() {
        super("<#JAVALETTERORDIGIT#>");
    }

    @Override
    public void match() {
        String curchar = String.valueOf(JavaEngine.parser.getCurrentTokenChar());
        if (curchar.matches("^[a-zA-Z0-9]$")) {
            JavaEngine.parser.readCurrentTokenChar();
        } else {
            throw new RuleNotMatchException();
        }
    }
}