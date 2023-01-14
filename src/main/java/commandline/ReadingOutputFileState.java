package commandline;

public class ReadingOutputFileState implements CommandLineArgState {
    @Override
    public boolean parseArg(String arg, CommandLineArgsParser parser) {
        parser.setOutputFile(arg);
        parser.setCurrentState(parser.getReadingInputFilesState());
        return true;
    }

    @Override
    public CommandLineArgState nextState(CommandLineArgsParser parser) {
        return parser.getReadingInputFilesState();
    }
}