package cpp.to.java;

import cpp.to.java.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * [  key ][        value          ]
 *  ______  _______________________
 * | name || type | const | static |
 * |------||-----------------------|
 * |      ||      |       |        |
 * |______||______|_______|________|
 */
public class VariablesMap {
    public static HashMap<String, String[]> generateMap(ArrayList<String> data) {
        HashMap<String, String[]> varMap = new HashMap<>();

        if (data.size() == 0) {
            return varMap;
        }

        // Generate map from function header
        if (data.get(0).contains("(") && data.get(0).contains(")")) {
            String[] fHeader = data.get(0).substring(data.get(0).indexOf('(') + 1, data.get(0).lastIndexOf(')')).split(",");
            for (String line : fHeader) {
                Pair<String, String[]> vMap = getMap(line.trim());
                if (vMap.getKey().length() != 0) {
                    varMap.put(vMap.getKey(), vMap.getValue());
                }
            }
        }

        // Generate map from function body
        for (String line : data) {
            Pair<String, String[]> vMap = getMap(line);
            if (vMap.getKey().length() != 0) {
                varMap.put(vMap.getKey(), vMap.getValue());
            }
        }

        return varMap;
    }

    private static Pair<String, String[]> getMap(String line) {
        String key = ""; // variable name
        String[] value = {"", "", ""}; // type, const, static

        Pattern p = Pattern.compile("[a-zA-z]+\\s+[a-zA-Z]+");
        Matcher m = p.matcher(line);

        if (m.find()) {
            String[] statement = line.split(" ");

            if (statement[0].equals("private")
                    || statement[0].equals("protected")
                    || statement[0].equals("public")
                    || statement[0].equals("return")
                    || !statement[0].matches("[a-zA-Z0-9]+"))
                return new Pair<String, String[]>(key, value);

            for (int i = 0; i < statement.length; ++i) {
                if (statement[i].equals("const")) {
                    value[1] = "const";
                } else if (statement[i].equals("static")) {
                    value[2] = "static";
                } else {
                    value[0] = statement[i];
                    key = statement[i + 1].split(";")[0].split("=")[0];
                    break;
                }
            }
        }
        return new Pair<String, String[]>(key, value);
    }
}