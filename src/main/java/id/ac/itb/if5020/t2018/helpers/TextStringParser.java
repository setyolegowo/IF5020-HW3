package id.ac.itb.if5020.t2018.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class TextStringParser extends TextFileParser {

    public TextStringParser(String string) throws IOException {
        super(new BufferedReader(new StringReader(string)));
    }
}