package cpp.to.java;

import java.util.ArrayList;

public class MainMethodClass {
    public static ArrayList<String> mainMethodClass(ArrayList<String> globalVariables, ArrayList<String> mainMethod) {
        ArrayList<String> mainClass = new ArrayList<>();

        mainClass.add("public class Main {\n");
        // Global variables
        if (globalVariables.size() != 0) {
            for (String s : globalVariables) {
                mainClass.add(s);
            }
            mainClass.add("\n");
        }

        for (String s : mainMethod) {
            mainClass.add(s);
        }
        mainClass.add("}\n");

        return mainClass;
    }
}