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
    private FileWriter javaFile;
    private Scanner cppFile;
    private String javaFileName;

    private ArrayList<String> mHeaders;
    private ArrayList<String> mMainClass;
    private ArrayList<String> mMainMethod;
    private ArrayList<ArrayList<String>> mMethods;

    private boolean canHaveMainMethod;
    private String prefix;

    CodeFileProcessor(String cppFile) {
        // Setup .cpp file reader
        try {
            this.cppFileObj = new File(cppFile);
            this.cppFile = new Scanner(cppFileObj);
        } catch (FileNotFoundException e) {
            Main.exit("Error: Unable to find " + cppFile);
        }

        // Setup .java file writer
        javaFileName = cppFile.substring(0, cppFile.lastIndexOf('.'));
        try {
            this.javaFile = new FileWriter(javaFileName + ".java");
        } catch (IOException e) {
            Main.exit("Error: Unable to create " + javaFileName + ".java");
        }

        mHeaders = new ArrayList<String>();
        mMainClass = new ArrayList<String>();
        mMainMethod = new ArrayList<String>();
        mMethods = new ArrayList<ArrayList<String>>();

        canHaveMainMethod = true;
        prefix = "public static";
    }

    CodeFileProcessor(String cppFile, ArrayList<String> mHeaders) {
        this(cppFile);
        this.mHeaders = mHeaders;
        this.canHaveMainMethod = false;
        this.prefix = "private";
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

                if (line.startsWith("public:")) {
                    prefix = "public";
                    continue;
                } else if (line.startsWith("protected:")) {
                    prefix = "protected";
                    continue;
                } else if (line.startsWith("private:")) {
                    prefix = "private";
                    continue;
                }

                // Main method
                if (canHaveMainMethod && (line.contains("int main(") || line.contains("void main("))) {
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

                // Class
                if (line.startsWith("class")) {
                    String className = line.split(" ")[1].trim();

                    // Create new cpp file for class
                    FileWriter classFile = new FileWriter(className + ".cpp_x");

                    int count = 0;
                    if (line.contains("{")) count++;

                    // Write class to file
                    do {
                        line = cppFile.nextLine();
                        if (line.contains("};") && count == 1) {
                            break;
                        }
                        for (int i = 0; i < line.length(); ++i) {
                            if (line.charAt(i) == '{') count++;
                            if (line.charAt(i) == '}') count--;
                        }
                        classFile.write(line + "\n");
                    } while (count != 0);
                    classFile.close();

                    // Call Code Processor on newly created cpp file.
                    CodeFileProcessor cfp = new CodeFileProcessor(className + ".cpp_x", mHeaders);
                    cfp.start();
                    cfp.flush();
                    cfp.close();

                    // Delete cpp class file
                    File cFile = new File(className + ".cpp_x");
                    cFile.delete();
                    continue;
                }

                // Class methods
                String funcHeader = "[^0-9][a-zA-z0-9]+[\s]*[a-zA-Z]+[a-zA-Z0-9]*\\(([a-zA-Z]+[a-zA-z0-9]*[\s]+[a-zA-Z]+[a-zA-Z0-9]*)*(\\,[\\s]?[a-zA-Z]+[a-zA-z0-9]*[\s]+[a-zA-Z]+[a-zA-Z0-9]*)*\\)(\\s)*(\\{)?";
                if (Pattern.matches(funcHeader, line)) {
                    ArrayList<String> func = new ArrayList<>();
                    int count = 0;
                    func.add(prefix + " " + line + "\n");
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

                // Skip "using namespace std;"
                if (line.contains("using namespace std;"))
                    continue;

                // Other class variables
                if (line.length() != 0)
                    mMainClass.add(prefix + " " + line + "\n");
            }

            CodeWriter cw = new CodeWriter();
            ArrayList<String> javaCode = MainMethodClass.mainMethodClass(javaFileName, mMainClass, mMainMethod, mMethods);
            cw.write(Headers.getJavaHeaders(mHeaders, javaCode), javaFile);
            cw.write(javaCode, javaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}