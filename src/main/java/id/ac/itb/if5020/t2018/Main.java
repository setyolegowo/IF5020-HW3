package id.ac.itb.if5020.t2018;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import id.ac.itb.if5020.t2018.helpers.Marker;
import id.ac.itb.if5020.t2018.helpers.TextFileParser;

final public class Main {

    @Parameter(names = "-debug", description = "Debug mode")
    private boolean debug = false;

    @Parameter(names = "-loglevel", description = "Verbose log level")
    private Integer verbose = 1;

    @Parameter(names={"--filename", "-f"},description="File to be check",required=true)
    private String filename;

    /**
     *
     * @param args Terminal arguments.
     */
    public static void main(String[] args) {
        JavaEngine.LOGGER.setLevel(Level.OFF);

        Main main = new Main();
        try {
            JCommander.newBuilder().addObject(main).build().parse(args);
            main.prepare();
            main.run();
            if (JavaEngine.parser.isEndOfFile()) {
                System.out.println("File is parsed perfectly.");
            } else {
                main.printError();
            }
        } catch (Exception e) {
            JavaEngine.LOGGER.severe(e.getMessage());
        }
    }

    private void prepare() throws FileNotFoundException, IOException, ParseException {
        if (debug) {
            switch (verbose) {
                case 1:
                    JavaEngine.LOGGER.setLevel(Level.WARNING);
                    break;

                case 2:
                    JavaEngine.LOGGER.setLevel(Level.INFO);
                    break;

                default: break;
            }
        }

        JavaEngine.LOGGER.info("Starting prepare rules...");
        JavaEngine.prepareRules();
        JavaEngine.prepareFirstList();
        JavaEngine.LOGGER.info("Just finished prepare rules");

        JavaEngine.LOGGER.info("Prepare file parser");
        JavaEngine.parser = new TextFileParser(filename);
        JavaEngine.parser.reset();
    }

    private void run() throws ParseException {
        JavaEngine.LOGGER.info("Parser running...");
        JavaEngine.runProgram();
    }

    private void printError() {
        Marker mark = JavaEngine.parser.getLatestError();
        System.err.println("Parsing failed in line " + mark.lineNumber);
        System.err.println();
        System.err.println(JavaEngine.parser.getCurrentLineString());
        for (int i = 0; i < mark.colNumber - mark.shiftNumber; i++) {
            System.err.print(' ');
        }
        System.err.println('^');
    }
}