package cpp.to.java;

import java.util.ArrayList;

public class Statements {
    private boolean isScannerAdded = false;

    public String convert(String line, ArrayList<String[]> vMap) {
        String newLine = "";

        // Comment line
        if (line.startsWith("//")) {
            return line;
        }

        // Input taking statement
        if (line.contains("cin>>") || line.contains("cin >>")) {
            if (!isScannerAdded) {
                newLine += "Scanner input = new Scanner(System.in);\n";
                isScannerAdded = true;
            }

            String[] sSplit = line.split(">>");

            for (int i = 1; i < sSplit.length; ++i) {
                if (sSplit[i].contains(";")) {
                    sSplit[i] = sSplit[i].substring(0, sSplit[i].indexOf(";")).trim();
                }
                switch (getVarType(sSplit[i], vMap)) {
                    case "bool":
                        newLine += sSplit[i] + " = input.nextBoolean();\n";
                        break;
                    case "char":
                        newLine += sSplit[i] + " = input.next().charAt();\n";
                        break;
                    case "short":
                        newLine += sSplit[i] + " = input.nextShort();\n";
                        break;
                    case "int":
                        newLine += sSplit[i] + " = input.nextInt();\n";
                        break;
                    case "long":
                        newLine += sSplit[i] + " = input.nextLong();\n";
                        break;
                    case "float":
                        newLine += sSplit[i] + " = input.nextFloat();\n";
                        break;
                    case "double":
                        newLine += sSplit[i] + " = input.nextDouble();\n";
                        break;
                    default:
                        newLine += sSplit[i] + " = input.next();\n";
                        break;
                }
            }
            return newLine;
        }

        return line;
    }

    private String getVarType(String vName, ArrayList<String[]> vMap) {
        for (String[] s : vMap) {
            if (s[0].equals(vName)) {
                return s[1];
            }
        }
        return "";
    }
}