package cpp.to.java.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class StatementParser {
    private boolean isScannerAdded = false;

    public ArrayList<String> convert(String line, HashMap<String, String[]> vMap, HashMap<String, String[]> globalVMap) {
        ArrayList<String> newLine = new ArrayList<>();

        // Comment line
        if (line.startsWith("//")) {
            newLine.add(line);
            return newLine;
        }

        // Const variable
        if (line.contains("const ")) {
            line = line.replace("const ", "final ");
        }

        // String variable
        if (line.contains("string ")) {
            line = line.replace("string ", "String ");
        }

        // Input taking statement
        if (line.contains("cin>>") || line.contains("cin >>")) {
            if (!isScannerAdded) {
                newLine.add("Scanner input = new Scanner(System.in);\n");
                isScannerAdded = true;
            }

            String[] sSplit = line.split(">>");

            for (int i = 1; i < sSplit.length; ++i) {
                if (sSplit[i].contains(";")) {
                    sSplit[i] = sSplit[i].substring(0, sSplit[i].indexOf(";")).trim();
                }
                switch (getVarType(sSplit[i], vMap, globalVMap)) {
                    case "bool":
                        newLine.add(sSplit[i] + " = input.nextBoolean();");
                        break;
                    case "char":
                        newLine.add(sSplit[i] + " = input.next().charAt(0);");
                        break;
                    case "short":
                        newLine.add(sSplit[i] + " = input.nextShort();");
                        break;
                    case "int":
                        newLine.add(sSplit[i] + " = input.nextInt();");
                        break;
                    case "long":
                        newLine.add(sSplit[i] + " = input.nextLong();");
                        break;
                    case "float":
                        newLine.add(sSplit[i] + " = input.nextFloat();");
                        break;
                    case "double":
                        newLine.add(sSplit[i] + " = input.nextDouble();");
                        break;
                    default:
                        newLine.add(sSplit[i] + " = input.next();");
                        break;
                }
            }
            return newLine;
        }

        // Parse cout statement
        if (line.contains("cout <<") || line.contains("cout<<")) {
            String[] sSplit = line.split("<<");

            String newL = "System.out.print(";

            for (int i = 1; i < sSplit.length; ++i) {
                if (sSplit[i].contains(";")) {
                    sSplit[i] = sSplit[i].substring(0, sSplit[i].indexOf(";")).trim();
                } else {
                    sSplit[i] = sSplit[i].trim();
                }

                if (sSplit[i].equals("endl")) {
                    newL += " + \"\\n\"";
                } else {
                    newL += (i >= 2) ? (" + " + sSplit[i]) : sSplit[i];
                }
            }
            newL += ");";
            newLine.add(newL);
            return newLine;
        }

        // Parse class object instantiation
        if (!line.contains("=")
                && !isPrimitiveType(line.split(" ")[0])
                && !isKeyword(line.split(" ")[0])
                && line.split(" ")[0].matches("[a-zA-Z0-9]+")) {
            String type = line.split(" ")[0];
            String var = "";
            String init = "";

            if (line.contains("(")) {
                var = line.substring(line.indexOf(" ") + 1, line.indexOf("("));
                init = line.substring(line.indexOf("("), line.indexOf(";"));
            } else {
                var = line.substring(line.indexOf(" ") + 1, line.indexOf(";"));
                init = "()";
            }
            newLine.add(type + " " + var + " = new " + type + init + ";");
            return newLine;
        }

        newLine.add(line);
        return newLine;
    }

    private String getVarType(String vName, HashMap<String, String[]> vMap, HashMap<String, String[]> globalVMap) {
        // Look into local vMap
        if (vMap.get(vName) != null) {
            return vMap.get(vName)[0];
        }

        // Look into globalVMap if variable doesn't exits in local vMap
        if (globalVMap.get(vName) != null) {
            return globalVMap.get(vName)[0];
        }

        // Return empty string if variable not found in any vMap
        return "";
    }

    private boolean isPrimitiveType(String type) {
        switch (type) {
            case "boolean":
            case "char":
            case "double":
            case "float":
            case "int":
            case "long":
            case "short":
                return true;
            default:
                return false;
        }
    }

    private boolean isKeyword(String type) {
        switch (type) {
            case "break":
            case "case":
            case "const":
            case "default":
            case "do":
            case "else":
            case "final":
            case "for":
            case "if":
            case "private":
            case "protected":
            case "public":
            case "return":
            case "static":
            case "switch":
            case "while":
                return true;
            default:
                return false;
        }
    }
}