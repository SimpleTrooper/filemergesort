package commandline;

public interface CommandLineArgState {
    boolean parseArg(String arg, CommandLineArgsParser parser);

    CommandLineArgState nextState(CommandLineArgsParser parser);
}
