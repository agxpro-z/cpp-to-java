package cpp.to.java;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CodeWriter {
    private int indent;

    CodeWriter() {
        indent = 0;
    }

    public void write(ArrayList<String> code, FileWriter file) throws IOException {
        for (String s : code) {
            file.write(s);
        }
    }
}