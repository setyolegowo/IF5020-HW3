package id.ac.itb.if5020.t2018.helpers;

import java.io.IOException;

public interface TextFileParserInterface {
    public void reset() throws IOException;
    public String getCurrentToken();
    public char getCurrentTokenChar();
    public char readCurrentTokenChar();
    public String readNextToken();
    public int getCurrentLineNumber();
    public String getCurrentLineString();
    public int getCurrentCol();
    public Marker getMarker();
    public void resetToMarker(Marker marker);
    public boolean isEndOfFile();
    public void markError();
    public Marker getLatestError();
}