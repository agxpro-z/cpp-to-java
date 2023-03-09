package cpp.to.java;

import java.util.ArrayList;

public class Headers {
    public static ArrayList<String> getJavaHeaders(ArrayList<String> headers) {
        ArrayList<String> javaHeaders = new ArrayList<>();

        for (String s : headers) {
            if (s.contains("iostream")) {
                javaHeaders.add("import java.util.Scanner;\n");
                continue;
            }
        }

        return javaHeaders;
    }
}