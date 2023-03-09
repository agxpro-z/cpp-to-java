package cpp.to.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;

public class CodeFileProcessor {
    private File cppFileObj;
    private Scanner cppFile;
    private FileWriter javaFile;

    private ArrayList<String> mHeaders;
    private ArrayList<String> mMainClass;
    private ArrayList<String> mMainMethod;
    private ArrayList<ArrayList<String>> mMethods;

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
        mMainClass = new ArrayList<String>();
        mMainMethod = new ArrayList<String>();
        mMethods = new ArrayList<ArrayList<String>>();
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

                // Headers
                if (line.startsWith("#include")) {
                    mHeaders.add(line + "\n");
                    continue;
                }

                // Main method
                if (line.contains("int main(") || line.contains("void main(")) {
                    int count = 0;
                    mMainMethod.add("public static void main(String[] args) {\n");
                    if (line.contains("{")) count++;

                    do {
                        line = cppFile.nextLine().trim();
                        for (int i = 0; i < line.length(); ++i) {
                            if (line.charAt(i) == '{') count++;
                            if (line.charAt(i) == '}') count--;
                        }
                        mMainMethod.add(line + "\n");
                    } while (count != 0);
                    continue;
                }

                // Main class methods
                String funcHeader = "[^0-9][a-zA-z0-9]+[\s]*[a-zA-Z]+[a-zA-Z0-9]*\\(([a-zA-Z]+[a-zA-z0-9]*[\s]+[a-zA-Z]+[a-zA-Z0-9]*)*(\\,[\\s]?[a-zA-Z]+[a-zA-z0-9]*[\s]+[a-zA-Z]+[a-zA-Z0-9]*)*\\)(\\s)*(\\{)?";
                if (Pattern.matches(funcHeader, line)) {
                    ArrayList<String> func = new ArrayList<>();
                    int count = 0;
                    func.add("public static " + line + "\n");
                    if (line.contains("{")) count++;

                    do {
                        line = cppFile.nextLine().trim();
                        for (int i = 0; i < line.length(); ++i) {
                            if (line.charAt(i) == '{') count++;
                            if (line.charAt(i) == '}') count--;
                        }
                        func.add(line + "\n");
                    } while (count != 0);
                    mMethods.add(func);
                    continue;
                }

                // Main Class
                if (line.length() != 0)
                    mMainClass.add(line + "\n");
            }

            for (String s : Headers.getJavaHeaders(mHeaders)) {
                javaFile.write(s);
            }

            for (String s : MainMethodClass.mainMethodClass(mMainClass, mMainMethod, mMethods)) {
                javaFile.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}