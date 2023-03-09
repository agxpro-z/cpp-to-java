package cpp.to.java;

import java.util.ArrayList;

public class MainMethodClass {
    public static ArrayList<String> mainMethodClass(ArrayList<String> globalVariables,
                                                    ArrayList<String> mainMethod,
                                                    ArrayList<ArrayList<String>> methods) {

        ArrayList<String> mainClass = new ArrayList<>();

        mainClass.add("public class Main {\n");
        // Global variables
        if (globalVariables.size() != 0) {
            for (String s : globalVariables) {
                mainClass.add(s);
            }
            mainClass.add("\n");
        }

        // Main class methods
        if (methods.size() != 0) {
            for (ArrayList<String> func : methods) {
                for (String s : func) {
                    mainClass.add(s);
                }
                mainClass.add("\n");
            }
        }

        // Main method
        for (String s : mainMethod) {
            mainClass.add(s);
        }
        mainClass.add("}\n");

        return mainClass;
    }
}