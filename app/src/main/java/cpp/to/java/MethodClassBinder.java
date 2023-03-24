package cpp.to.java;

import cpp.to.java.parser.StatementParser;
import cpp.to.java.util.VariablesMap;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodClassBinder {
    public static ArrayList<String> bindMethodClass(String className,
                                                    ArrayList<String> globalVariables,
                                                    ArrayList<String> mainMethod,
                                                    ArrayList<ArrayList<String>> methods) {

        ArrayList<String> mainClass = new ArrayList<>();
        HashMap<String, String[]> globalVMap = VariablesMap.generateMap(globalVariables);

        // Return empty list if no lines are present
        if (globalVariables.size() == 0
            && mainMethod.size() == 0
            && methods.size() == 0) return mainClass;

        mainClass.add("public class " + className + " {\n");
        // Global variables
        if (globalVariables.size() != 0) {
            for (String s : globalVariables) {
                if (s.contains("const "))
                    s = s.replace("const ", "final ");
                mainClass.add(s);
            }
            mainClass.add("\n");
        }

        // Main class methods
        if (methods.size() != 0) {
            for (ArrayList<String> func : methods) {
                HashMap<String, String[]> localVMap = VariablesMap.generateMap(func);
                StatementParser statement = new StatementParser();

                for (String s : func) {
                    mainClass.addAll(statement.convert(s, localVMap, globalVMap));
                }
                mainClass.add("\n");
            }
        }

        // Main method
        HashMap<String, String[]> mainMethodVMap = VariablesMap.generateMap(mainMethod);
        StatementParser statement = new StatementParser();
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