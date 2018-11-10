package id.ac.itb.if5020.t2018.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import id.ac.itb.if5020.t2018.JavaEngine;

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
    public String getCurrentToken() {
        return currentToken;
    }

    private String currentLine;

    @Override
    public String getCurrentLineString() {
        return currentLine;
    }

    @Override
    public String readNextToken() {
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

    private void updateCurrentToken() {
        shiftedSymbol = 0;
        currentTokenIndex = 0;
        currentToken = "";

        for (;currentCol + shiftedSymbol < currentLine.length();) {
            if (shiftedSymbol == 0) {
                if (currentLine.charAt(currentCol + shiftedSymbol) <= ' ') {
                    currentCol++;
                    continue;
                }
            }
            while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("[a-zA-Z0-9]+")) {
                shiftedSymbol++;
            }
            if (shiftedSymbol == 0) {
                if (handleNonJavaLetterAndDigit()) {
                    break;
                }
            } else {
                currentToken = currentLine.substring(currentCol, currentCol + shiftedSymbol);
                currentCol += shiftedSymbol;
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
    private boolean handleNonJavaLetterAndDigit() {
        // Separate reading possibility
        // - Start reading string value
        // - Start reading multiline or tagged comment
        // - Start reading single line comment
        // - Start reading terminal with symbol count more than 1
        if (currentLine.substring(currentCol).matches("^\\..*")) {
            if (currentLine.substring(currentCol).matches("^\\.{3}[^\\.].*")) {
                currentToken = currentLine.substring(currentCol, currentCol + 3);
                currentCol += 3;
                shiftedSymbol = 3;
                return true;
            }

            currentToken = currentLine.substring(currentCol, currentCol + 1);
            currentCol += 1;
            shiftedSymbol = 1;
            return true;
        }
        if (currentLine.substring(currentCol).matches("^[\\*;].*")) {
            currentToken = currentLine.substring(currentCol, currentCol + 1);
            currentCol += 1;
            shiftedSymbol = 1;
            return true;
        }
        //
        return false;
    }

    public void close() throws IOException {
        bufferReader.close();
        if (filereader != null) {
            filereader.close();
        }
    }

    private int currentTokenIndex = 0;

    @Override
    public char getCurrentTokenChar() {
        return currentToken.charAt(currentTokenIndex);
    }

    @Override
    public char readCurrentTokenChar() {
        char retval = currentToken.charAt(currentTokenIndex++);
        if (currentToken.length() == currentTokenIndex) {
            readNextToken();
        }
        return retval;
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

    private Marker errorMarker;

    @Override
    public void markError() {
        if (errorMarker != null) {
            errorMarker = null;
        }

        errorMarker = new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public Marker getLatestError() {
        return errorMarker;
    }
}