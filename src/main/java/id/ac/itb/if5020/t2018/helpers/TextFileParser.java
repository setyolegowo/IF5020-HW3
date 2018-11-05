package id.ac.itb.if5020.t2018.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextFileParser {

    private FileReader filereader;

    private final BufferedReader bufferReader;

    public TextFileParser(String _filename) throws FileNotFoundException {
        filereader = new FileReader(_filename);
        bufferReader = new BufferedReader(filereader);
    }

    public TextFileParser(File file) throws FileNotFoundException {
        filereader = new FileReader(file);
        bufferReader = new BufferedReader(filereader);
    }

    public TextFileParser(FileReader reader) {
        bufferReader = new BufferedReader(reader);
    }

    public TextFileParser(BufferedReader br) {
        bufferReader = br;
    }

    public String readNextToken() {
        // TODO
        return null;
    }

    public void close() throws IOException {
        bufferReader.close();
        if (filereader != null) {
            filereader.close();
        }
    }
}