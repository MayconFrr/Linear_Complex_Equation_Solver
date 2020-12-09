package argParser;

public class OptionalArgumentParser {
    private OptionalArgumentParser() {
    }

    public static String parseArgument(String[] args, String argument) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argument))
                if (i + 1 == args.length || args[i + 1].startsWith("-")) {
                    return "";
                } else {
                    return args[i + 1];
                }
        }
        return null;
    }
}
