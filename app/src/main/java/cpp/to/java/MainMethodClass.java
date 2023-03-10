package cpp.to.java;

import java.util.ArrayList;

public class MainMethodClass {
    public static ArrayList<String> mainMethodClass(String className,
                                                    ArrayList<String> globalVariables,
                                                    ArrayList<String> mainMethod,
                                                    ArrayList<ArrayList<String>> methods) {

        ArrayList<String> mainClass = new ArrayList<>();
        ArrayList<String[]> globalVMap = VariablesMap.generateMap(globalVariables);

        // Return empty list if no lines are present
        if (globalVariables.size() == 0
            && mainMethod.size() == 0
            && methods.size() == 0) return mainClass;

        mainClass.add("public class " + className + " {\n");
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
                    mainClass.addAll(statement.convert(s, localVMap, globalVMap));
                }
                mainClass.add("\n");
            }
        }

        // Main method
        ArrayList<String[]> mainMethodVMap = VariablesMap.generateMap(mainMethod);
        Statements statement = new Statements();
        for (String s : mainMethod) {
            if (s.contains("return 0;")) {
                continue;
            }
            mainClass.addAll(statement.convert(s, mainMethodVMap, globalVMap));
        }

        // Remove extra line before class end
        if (mainClass.get(mainClass.size() - 1).equals("\n"))
            mainClass.remove(mainClass.size() - 1);
        mainClass.add("}\n");

        return mainClass;
    }
}