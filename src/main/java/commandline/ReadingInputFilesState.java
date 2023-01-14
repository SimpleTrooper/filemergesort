package commandline;

public class ReadingInputFilesState implements CommandLineArgState {
    @Override
    public boolean parseArg(String arg, CommandLineArgsParser parser) {
        parser.addInputFile(arg);
        return true;
    }

    @Override
    public CommandLineArgState nextState(CommandLineArgsParser parser) {
        return parser.getReadingInputFilesState();
    }
}
