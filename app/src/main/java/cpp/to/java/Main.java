package cpp.to.java;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) { return; }

        if (!args[0].substring(args[0].lastIndexOf('.')).equals(".cpp")) {
            invalidFileType(args[0]);
        }
    }

    private static void invalidFileType(String fileName) {
        System.out.println("Invalid file type: " + fileName);
    }
}
