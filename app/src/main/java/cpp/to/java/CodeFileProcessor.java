package cpp.to.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CodeFileProcessor {
    private File cppFileObj;
    private Scanner cppFile;
    private FileWriter javaFile;

    CodeFileProcessor(String cppFile) {
        // Setup .cpp file reader
        try {
            this.cppFileObj = new File(cppFile);
            this.cppFile = new Scanner(cppFileObj);
        } catch (FileNotFoundException e) {
            Main.exit("Error: Unable to find " + cppFile);
        }

        // Setup .java file writer
        String javaFileName = cppFile.substring(0, cppFile.lastIndexOf('.')) + ".java";
        try {
            this.javaFile = new FileWriter(javaFileName);
        } catch (IOException e) {
            Main.exit("Error: Unable to create " + javaFileName);
        }
    }

    public void flush() {
        try {
            javaFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}