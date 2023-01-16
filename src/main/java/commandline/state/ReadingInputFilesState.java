package commandline.state;

import commandline.CommandLineArgsParser;

public class ReadingInputFilesState implements CommandLineArgState {
    @Override
    public boolean parseArg(String arg, CommandLineArgsParser parser) {
        parser.addInputFile(arg);
        return true;
    }
}
