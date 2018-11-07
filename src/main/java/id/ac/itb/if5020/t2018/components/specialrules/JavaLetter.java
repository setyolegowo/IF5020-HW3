package id.ac.itb.if5020.t2018.components.specialrules;

import id.ac.itb.if5020.t2018.components.SpecialRule;
import id.ac.itb.if5020.t2018.components.symbols.NonTerminalSymbol;

public class JavaLetter extends NonTerminalSymbol implements SpecialRule {

    public JavaLetter() {
        super("#JAVALETTER#");
    }
}