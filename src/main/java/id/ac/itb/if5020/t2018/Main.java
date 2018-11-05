package id.ac.itb.if5020.t2018;

import java.lang.Exception;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

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

    public void prepare() {
        // TODO
    }

    public void run() {
        // TODO
    }
}