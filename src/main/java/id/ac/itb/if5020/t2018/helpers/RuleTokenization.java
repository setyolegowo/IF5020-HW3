
package id.ac.itb.if5020.t2018.helpers;

import java.text.ParseException;
import java.util.Stack;

public class RuleTokenization {

    public final String needle;

    private int position = 0;

    public RuleTokenization(String _string) {
        needle = _string;
        position = 0;
    }

    public void startOver() {
        position = 0;
    }

    public boolean hasNext() {
        return needle.length() > position;
    }

    public String readNextToken() throws ParseException {
        int startPosition = position;
        String retval = "";
        char lookUp;
        Stack<String> stack = new Stack<String>();

        while(needle.length() > position && (needle.charAt(position) != ' ' || !stack.empty())) {
            lookUp = needle.charAt(position);
            retval += lookUp;

            switch (lookUp) {
                case '[':
                    stack.push("]");
                    break;
                case '{':
                    stack.push("}");
                    break;
            }

            if (!stack.empty() && lookUp == stack.peek().charAt(0)) {
                stack.pop();
            }

            position++;
        }
        if (!stack.empty()) {
            throw new ParseException("Rule '" + needle + "' cannot be parsed.", startPosition);
        }
        if (needle.charAt(position) == ' ') {
            position++;
        }
        if (retval.length() == 0 && needle.length() > position) {
            if (needle.length() > position) {
                throw new ParseException("Token should not empty.", position);
            }
            return null;
        }

        return retval;
    }
}