package cpp.to.java;

import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            usageHelp();
            return;
        }

        if (!args[0].contains(".") || !args[0].substring(args[0].lastIndexOf('.')).equals(".cpp")) {
            exit("Invalid file type: " + args[0]);
        }

        System.out.println("IN: " + args[0]); // Print input file name

        CodeProcessor processor = new CodeProcessor(args[0]);
        processor.start();
        processor.flush();
        processor.close(); // Close opened files before exit.
    }

    /*
     * Exit method to exit program in between
     *
     * @param message = message to print before exit.
     */
    public static void exit(String message) {
        if (message.length() == 0) { System.exit(0); }

        System.err.println(message);
        System.exit(1);
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

        System.out.print("Usage: \n    ");
        System.out.print(jarFileName.substring(jarFileName.lastIndexOf('/') + 1));
        System.out.println(" <file-name>.cpp");
    }
}
