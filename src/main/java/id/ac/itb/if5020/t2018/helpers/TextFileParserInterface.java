package id.ac.itb.if5020.t2018.helpers;

import java.io.IOException;

public interface TextFileParserInterface {
    public void reset() throws IOException;
    public String getCurrentToken();
    public String readNextToken();
    public int getCurrentLineNumber();
    public int getCurrentCol();
}