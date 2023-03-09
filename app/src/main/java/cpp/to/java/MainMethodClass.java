package cpp.to.java;

import java.util.ArrayList;

public class MainMethodClass {
    public static ArrayList<String> mainMethodClass(ArrayList<String> globalVariables,
                                                    ArrayList<String> mainMethod,
                                                    ArrayList<ArrayList<String>> methods) {

        ArrayList<String> mainClass = new ArrayList<>();
        ArrayList<String[]> globalVMap = VariablesMap.generateMap(globalVariables);

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
                ArrayList<String[]> localVMap = VariablesMap.generateMap(func);
                Statements statement = new Statements();
                for (String s : func) {
                    mainClass.add(statement.convert(s, localVMap, globalVMap));
                }
                mainClass.add("\n");
            }
        }

        // Main method
        ArrayList<String[]> mainMethodVMap = VariablesMap.generateMap(mainMethod);
        Statements statement = new Statements();
        for (String s : mainMethod) {
            mainClass.add(statement.convert(s, mainMethodVMap, globalVMap));
        }
        mainClass.add("}\n");

        return mainClass;
    }
}