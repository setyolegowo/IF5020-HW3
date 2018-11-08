package id.ac.itb.if5020.t2018.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextFileParser implements TextFileParserInterface {

    private FileReader filereader;

    private final BufferedReader bufferReader;

    private int lineNumber = 0;

    private int currentCol = 0;

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
        return currentCol;
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
    public String readNextToken() {
        if (currentLine == null) {
            try {
                currentLine = bufferReader.readLine();
                lineNumber++;
                currentCol = 0;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        updateCurrentToken();
        return currentToken;
    }

    private int shiftedSymbol;

    private void updateCurrentToken() {
        shiftedSymbol = 0;
        currentToken = null;
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
        //
        return false;
    }

    public void close() throws IOException {
        bufferReader.close();
        if (filereader != null) {
            filereader.close();
        }
    }
}