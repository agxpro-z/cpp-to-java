package cpp.to.java;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*  ______________________________
 * | name | type | const | static |
 * |------------------------------|
   |      |      |       |        |
 * |______|______|_______|________|
 */
public class VariablesMap {
    public static ArrayList<String[]> generateMap(ArrayList<String> data) {
        ArrayList<String[]> varMap = new ArrayList<>();

        if (data.size() == 0) {
            return varMap;
        }

        // Generate map from function header
        if (data.get(0).contains("(") && data.get(0).contains(")")) {
            String[] fHeader = data.get(0).substring(data.get(0).indexOf('(') + 1, data.get(0).lastIndexOf(')')).split(",");
            for (String line : fHeader) {
                String[] vMap = getMap(line.trim());
                if (vMap[0].length() != 0) {
                    varMap.add(vMap);
                }
            }
        }

        // Generate map from function body
        for (String line : data) {
            String[] vMap = getMap(line);
            if (vMap[0].length() != 0) {
                varMap.add(vMap);
            }
        }

        return varMap;
    }

    private static String[] getMap(String line) {
        String[] vMap = {"", "", "", ""}; // size 4

        Pattern p = Pattern.compile("[a-zA-z]+\\s+[a-zA-Z]+");
        Matcher m = p.matcher(line);

        if (m.find()) {
            String[] statement = line.split(" ");
            for (int i = 0; i < statement.length; ++i) {
                if (statement[i].equals("const")) {
                    vMap[2] = "const";
                } else if (statement[i].equals("static")) {
                    vMap[3] = "static";
                } else {
                    vMap[1] = statement[i];
                    vMap[0] = statement[i + 1].split(";")[0].split("=")[0];
                    break;
                }
            }
        }
        return vMap;
    }
}