package cpp.to.java.io;

import cpp.to.java.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CodeWriter {
    private int indent;
    private boolean wasPrevLineWithoutSemicolon = false;
    private FileWriter file;
    private String fileName;

    public CodeWriter(String fileName) {
        this.fileName = fileName;
        try {
            this.file = new FileWriter(fileName + ".java");
        } catch (IOException e) {
            Main.exit("Error: Unable to create " + fileName + ".java");
        }
        indent = 0;
    }

    public void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(ArrayList<String> code) {
        for (String s : code) {
            String space = "";
            for (int i = 0; i < (s.contains("}") ? indent - 1 : indent); ++i) {
                space += "    ";
                if (s.contains("};")) {
                    space += "    ";
                }
            }

            if (wasPrevLineWithoutSemicolon)
                space += "    ";

            if (((s.endsWith(")") || s.endsWith(":"))
                    && (s.startsWith("if") || s.startsWith("while")) || s.startsWith("case") || s.startsWith("default") || (s.startsWith("for") && !s.endsWith("{")))
                    || s.endsWith("else\n")) {
                wasPrevLineWithoutSemicolon = true;
            } else {
                wasPrevLineWithoutSemicolon = false;
            }

            for (int i = 0; i < s.length(); ++i) {
                if (s.charAt(i) == '{') indent++;
                if (s.charAt(i) == '}') indent--;
            }

            try {
                if (s.length() == 0) {
                    file.write(s + "\n");
                } else {
                    file.write(space + s + "\n");
                }
            } catch (IOException e) {
                System.err.println("Unable to write \"" + s + "\" to " + fileName + ".java");
            }
        }
    }
}