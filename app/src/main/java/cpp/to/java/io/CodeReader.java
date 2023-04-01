package cpp.to.java.io;

import cpp.to.java.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeReader {
    private File cppFile;
    private Scanner reader;

    public CodeReader(String cppFile) {
        try {
            this.cppFile = new File(cppFile);
            this.reader = new Scanner(this.cppFile);
        } catch (FileNotFoundException e) {
            Main.exit("Error: Unable to find " + cppFile);
        }
    }

    public ArrayList<String> read() {
        ArrayList<String> code = new ArrayList<String>();

        while (reader.hasNextLine()) {
            code.add(reader.nextLine().trim());
        }

        reader.close();

        return code;
    }
}