package id.ac.itb.if5020.t2018.helpers;

import java.io.IOException;
import java.text.ParseException;

import id.ac.itb.if5020.t2018.components.RuleNotMatchException;

public interface TextFileParserInterface {
    public void reset() throws IOException;
    public String getCurrentToken() throws ParseException;
    public char getCurrentTokenChar() throws ParseException;
    public char readCurrentTokenChar() throws ParseException;
    public String readNextToken() throws ParseException;
    public int getCurrentLineNumber();
    public String getCurrentLineString();
    public int getCurrentCol();
    public Marker getMarker();
    public void resetToMarker(Marker marker);
    public boolean isEndOfFile();
    public void markError(RuleNotMatchException _exception);
    public void markError();
    public RuleNotMatchException getLastRuleException();
    public Marker getLatestError();
}