package cpp.to.java.parser;

import java.util.ArrayList;

public class HeaderParser {
    public static ArrayList<String> getJavaHeaders(ArrayList<String> headers, ArrayList<String> javaCode) {
        ArrayList<String> javaHeaders = new ArrayList<>();

        // Return empty list if no headers are present
        if (headers.size() == 0)
            return headers;

        for (String code : generateHeaders(headers)) {
            if (isHeaderRequired(code.substring(code.lastIndexOf(".") + 1, code.lastIndexOf(";")), javaCode)) {
                javaHeaders.add(code);
            }
        }

        if (javaHeaders.size() != 0)
            javaHeaders.add("\n");
        return javaHeaders;
    }

    private static ArrayList<String> generateHeaders(ArrayList<String> headers) {
        ArrayList<String> javaHeaders = new ArrayList<>();
        for (String s : headers) {
            if (s.contains("iostream")) {
                javaHeaders.add("import java.util.Scanner;\n");
                continue;
            }
        }
        return javaHeaders;
    }

    private static boolean isHeaderRequired(String header, ArrayList<String> javaCode) {
        for (String code : javaCode) {
            if (code.contains(header))
                return true;
        }
        return false;
    }
}