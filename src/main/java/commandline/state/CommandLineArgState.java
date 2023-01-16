package commandline.state;

import commandline.CommandLineArgsParser;

public interface CommandLineArgState {
    boolean parseArg(String arg, CommandLineArgsParser parser);
}
