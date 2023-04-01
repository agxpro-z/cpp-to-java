package cpp.to.java;

import cpp.to.java.io.CodeWriter;
import cpp.to.java.parser.HeaderParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;

public class CodeProcessor {
    private String fileName;

    private ArrayList<String> mCode;
    private ArrayList<String> mClass;
    private ArrayList<String> mHeaders;
    private ArrayList<String> mMainMethod;
    private ArrayList<ArrayList<String>> mMethods;

    private boolean canHaveMainMethod;
    private String prefix;

    CodeProcessor(ArrayList<String> codeData, String fileName) {
        mHeaders = new ArrayList<String>();
        mClass = new ArrayList<String>();
        mMainMethod = new ArrayList<String>();
        mMethods = new ArrayList<ArrayList<String>>();

        this.fileName = fileName;
        this.mCode = codeData;

        canHaveMainMethod = true;
        prefix = "public static";
    }

    CodeProcessor(ArrayList<String> codeData, String fileName, ArrayList<String> mHeaders) {
        this(codeData, fileName);
        this.mHeaders = mHeaders;

        this.canHaveMainMethod = false;
        this.prefix = "private";
    }

    public void start() {
        String line = "";
        for (int index = 0; index < mCode.size(); ++index) {
            line = mCode.get(index);

            // Headers
            if (line.startsWith("#include")) {
                mHeaders.add(line);
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
                mMainMethod.add("public static void main(String[] args) {");
                if (line.contains("{")) count++;

                do {
                    line = mCode.get(++index);
                    for (int i = 0; i < line.length(); ++i) {
                        if (line.charAt(i) == '{') count++;
                        if (line.charAt(i) == '}') count--;
                    }
                    mMainMethod.add(line);
                } while (count != 0);
                continue;
            }

            // Class
            if (line.startsWith("class")) {
                String className = line.split(" ")[1].trim();
                ArrayList<String> classCode = new ArrayList<>();

                int count = 0;
                if (line.contains("{")) count++;

                // Write class to file
                do {
                    line = mCode.get(++index);
                    if (line.contains("};") && count == 1) {
                        break;
                    }
                    for (int i = 0; i < line.length(); ++i) {
                        if (line.charAt(i) == '{') count++;
                        if (line.charAt(i) == '}') count--;
                    }
                    classCode.add(line);
                } while (count != 0);

                // Call Code Processor on newly created cpp file.
                CodeProcessor cp = new CodeProcessor(classCode, className, mHeaders);
                cp.start();
                continue;
            }

            // Class methods
            String funcHeader = "(static\s)?(const\s)?[^0-9][a-zA-z0-9]+[\s]*[a-zA-Z]+[a-zA-Z0-9]*\\(([a-zA-Z]+[a-zA-z0-9]*[\s]+[a-zA-Z]+[a-zA-Z0-9]*)*(\\,[\s]*[a-zA-Z]+[\s]*[a-zA-Z]+[a-zA-z0-9]*[\s]+[a-zA-Z]+[a-zA-Z0-9]*)*\\)(\\s)*(\\{)?";
            if (Pattern.matches(funcHeader, line)) {
                ArrayList<String> func = new ArrayList<>();
                int count = 0;
                func.add(prefix + " " + line);
                if (line.contains("{")) count++;

                do {
                    line = mCode.get(++index);
                    for (int i = 0; i < line.length(); ++i) {
                        if (line.charAt(i) == '{') count++;
                        if (line.charAt(i) == '}') count--;
                    }
                    func.add(line);
                } while (count != 0);
                mMethods.add(func);
                continue;
            }

            // Skip "using namespace std;"
            if (line.contains("using namespace std;"))
                continue;

            // Remove leftover braces from class file creation
            if (line.equals("{") || line.equals("}"))
                continue;

            // Comment line
            if (line.startsWith("//")) {
                mClass.add(line);
                continue;
            }

            // Other class variables
            if (line.length() != 0)
                mClass.add(prefix + " " + line);
        }

        ArrayList<String> javaCode = MethodClassBinder.bindMethodClass(fileName, mClass, mMainMethod, mMethods);
        CodeWriter cw = new CodeWriter(fileName);
        cw.write(HeaderParser.getJavaHeaders(mHeaders, javaCode));
        cw.write(javaCode);
        cw.flush();
        cw.close();

        System.out.println("OUT: " + fileName + ".java");
    }
}