package id.ac.itb.if5020.t2018.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;

public class TextFileParser implements TextFileParserInterface {

    private FileReader filereader;

    private final BufferedReader bufferReader;

    private int lineNumber = 0;

    private int currentCol = 0;

    private boolean _isEndOfFile = false;

    public TextFileParser(String _filename) throws FileNotFoundException, IOException {
        filereader = new FileReader(_filename);
        bufferReader = new BufferedReader(filereader);
        bufferReader.mark(0);
    }

    public TextFileParser(File file) throws FileNotFoundException, IOException {
        filereader = new FileReader(file);
        bufferReader = new BufferedReader(filereader);
        bufferReader.mark(0);
    }

    public TextFileParser(FileReader reader) throws IOException {
        this(new BufferedReader(reader));
    }

    public TextFileParser(BufferedReader br) throws IOException {
        bufferReader = br;
        bufferReader.mark(0);
    }

    public int getCurrentLineNumber() {
        return lineNumber;
    }

    public int getCurrentCol() {
        return currentCol - shiftedSymbol;
    }

    @Override
    public void reset() throws IOException {
        bufferReader.reset();
    }

    private String currentToken;

    @Override
    public String getCurrentToken() throws ParseException {
        return currentToken;
    }

    private String currentLine;

    @Override
    public String getCurrentLineString() {
        return currentLine;
    }

    @Override
    public String readNextToken() throws ParseException {
        for (;;) {
            if (currentLine == null) {
                try {
                    currentLine = bufferReader.readLine();
                    lineNumber++;
                    currentCol = 0;
                    currentToken = null;

                    if (currentLine == null) {
                        _isEndOfFile = true;
                        return null;
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }

            updateCurrentToken();
            if (currentToken != null && currentToken != "") {
                break;
            }
        }

        return currentToken;
    }

    private int shiftedSymbol;

    private void updateCurrentToken() throws ParseException {
        shiftedSymbol = 0;
        currentTokenIndex = 0;
        currentToken = "";

        for (;currentCol + shiftedSymbol < currentLine.length();) {
            // Skip spacing
            if (shiftedSymbol == 0 && currentLine.charAt(currentCol + shiftedSymbol) <= ' ') {
                currentCol++;
                continue;
            }
            if (handleTokenization()) {
                break;
            }
        }

        if (currentCol == currentLine.length()) {
            currentLine = null;
        }
    }

    /**
     * @return true when there is token, false otherwise.
     */
    private boolean handleTokenization() throws ParseException {
        // Separate reading possibility
        // - Start reading string value
        if (currentLine.substring(currentCol).matches("^\".*")) {
            shiftedSymbol = 1;
            while (currentCol + shiftedSymbol < currentLine.length() && currentLine.charAt(currentCol + shiftedSymbol) != '"') {
                if (currentLine.charAt(currentCol + shiftedSymbol) == '\\') {
                    shiftedSymbol++;
                }
                shiftedSymbol++;
            }

            if (currentCol + shiftedSymbol >= currentLine.length()) {
                throw new ParseException("Failed parsing string literal \"" + currentLine + "\". String has no end?", currentCol);
            }
            shiftedSymbol++;
            return updateAfterTokenization();
        }
        // - Start reading multiline or tagged comment
        // - Start reading single line comment
        // - Start reading terminal with symbol count more than 1
        if (currentLine.substring(currentCol).matches("^=.*")) {
            if (currentLine.substring(currentCol).matches("^={2}[^=]*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            if (currentLine.substring(currentCol).matches("^={3}[^=]*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^<.*")) {
            if (currentLine.substring(currentCol).matches("^<{3}[^<]*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }
            if (currentLine.substring(currentCol).matches("^<=[^<=]*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^&.*")) {
            if (currentLine.substring(currentCol).matches("^&{2}[^&]*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^>.*")) {
            if (currentLine.substring(currentCol).matches("^>=[^>=]*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^\\..*")) {
            if (currentLine.substring(currentCol).matches("^\\.{3}[^\\.]*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^\\[\\].*")) {
            shiftedSymbol = 2;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^\\+.*")) {
            if (currentLine.substring(currentCol).matches("^\\+\\+[^\\+]*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^@.*")) {
            if (currentLine.substring(currentCol).matches("^@interface.*")) {
                shiftedSymbol = 10;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^[\\*,;{}()$^!~`\\?\\\\\\[\\]].*")) {
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        // - Start reading identifier
        if (currentLine.substring(currentCol).matches("^[a-zA-Z_].*")) {
            shiftedSymbol = 0;
            while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[a-zA-Z0-9_]+")) {
                shiftedSymbol++;
                if (currentCol + shiftedSymbol >= currentLine.length()) {
                    break;
                }
            }
            return updateAfterTokenization();
        }
        // - start reading integer or float
        if (currentLine.substring(currentCol).matches("^[0-9].*")) {
            // Simple integer
            shiftedSymbol = 0;
            while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[0-9]+")) {
                shiftedSymbol++;
                if (currentCol + shiftedSymbol >= currentLine.length()) {
                    break;
                }
            }
            return updateAfterTokenization();
        }
        return false;
    }

    private boolean updateAfterTokenization() {
        currentToken = currentLine.substring(currentCol, currentCol + shiftedSymbol);
        currentCol += shiftedSymbol;
        return true;
    }

    public void close() throws IOException {
        bufferReader.close();
        if (filereader != null) {
            filereader.close();
        }
    }

    private int currentTokenIndex = 0;

    @Override
    public char getCurrentTokenChar() throws ParseException {
        if (currentTokenIndex >= currentToken.length()) {
            readNextToken();
            return 0;
        }
        return currentToken.charAt(currentTokenIndex);
    }

    @Override
    public char readCurrentTokenChar() {
        if (currentTokenIndex >= currentToken.length()) {
            return 0;
        }
        return currentToken.charAt(currentTokenIndex++);
    }

    @Override
    public boolean isEndOfFile() {
        return _isEndOfFile;
    }

    @Override
    public Marker getMarker() {
        return new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public void resetToMarker(Marker marker) {
        JavaEngine.LOGGER.warning("This parser is not parsing with LL(1) rule.");

        if (marker.lineNumber != lineNumber) {
            throw new RuntimeException("Cannot reset no another line");
        }
        currentCol = marker.colNumber;
        shiftedSymbol = marker.shiftNumber;
        currentTokenIndex = marker.currentTokenIndex;
        currentToken = String.valueOf(marker.token);
    }

    private RuleNotMatchException errorException;

    private Marker errorMarker;

    @Override
    public void markError(RuleNotMatchException _exception) {
        errorException = _exception;
        if (errorMarker != null) {
            errorMarker = null;
        }

        errorMarker = new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public void markError() {
        if (errorMarker != null) {
            errorMarker = null;
        }

        errorMarker = new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public RuleNotMatchException getLastRuleException() {
        return errorException;
    }

    @Override
    public Marker getLatestError() {
        return errorMarker;
    }
}