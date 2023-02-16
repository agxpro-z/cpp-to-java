package cpp.to.java;

import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            usageHelp();
            return;
        }

        if (!args[0].substring(args[0].lastIndexOf('.')).equals(".cpp")) {
            invalidFileType(args[0]);
        }
    }

    private static void usageHelp() {
        String jarFileName = "";
        try {
            jarFileName = new String(Main.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.print("Usage: ");
        System.out.print(jarFileName.substring(jarFileName.lastIndexOf('/') + 1));
        System.out.println(" <fileName>.cpp");
    }

    private static void invalidFileType(String fileName) {
        System.out.println("Invalid file type: " + fileName);
    }
}
