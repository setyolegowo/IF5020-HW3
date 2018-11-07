package id.ac.itb.if5020.t2018;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Exception;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import id.ac.itb.if5020.t2018.helpers.TextFileParser;

final public class Main {

    @Parameter(names={"--filename", "-f"},description="File to be check",required=true)
    public String filename;

    /**
     *
     * @param args Terminal arguments.
     */
    public static void main(String[] args) throws Exception {
        JavaEngine.prepareRules();
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.prepare();
        main.run();
    }

    public void prepare() throws FileNotFoundException, IOException {
        JavaEngine.parser = new TextFileParser(filename);
        JavaEngine.parser.reset();
    }

    public void run() {
        JavaEngine.runProgram();
    }
}