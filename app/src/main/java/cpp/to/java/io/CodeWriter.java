package cpp.to.java.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CodeWriter {
    private int indent;

    public CodeWriter() {
        indent = 0;
    }

    public void write(ArrayList<String> code, FileWriter file) throws IOException {
        for (String s : code) {
            String space = "";
            for (int i = 0; i < (s.contains("}") ? indent - 1 : indent); ++i) {
                space += "    ";
            }

            for (int i = 0; i < s.length(); ++i) {
                if (s.charAt(i) == '{') indent++;
                if (s.charAt(i) == '}') indent--;
            }
            if (s.equals("\n")) {
                file.write(s);
            } else {
                file.write(space + s);
            }
        }
    }
}