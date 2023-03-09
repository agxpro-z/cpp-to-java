package cpp.to.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeFileProcessor {
    private File cppFileObj;
    private Scanner cppFile;
    private FileWriter javaFile;

    private ArrayList<String> mHeaders;

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

        mHeaders = new ArrayList<String>();
    }

    public void flush() {
        try {
            javaFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            cppFile.close();
            javaFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        String line = "";
        try {
            while (cppFile.hasNextLine()) {
                line = cppFile.nextLine().trim();
                if (line.startsWith("#include")) {
                    mHeaders.add(line + "\n");
                    continue;
                }
            }

            for (String s : Headers.getJavaHeaders(mHeaders)) {
                javaFile.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}