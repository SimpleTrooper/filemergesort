package commandline;

public class ReadingFlagsState implements CommandLineArgState {
    @Override
    public boolean parseArg(String arg, CommandLineArgsParser parser) {
        switch (arg) {
            case "-s" -> {
                parser.setDataType(DataType.STRING);
                return true;
            }
            case "-i" -> {
                parser.setDataType(DataType.INTEGER);
                return true;
            }
            case "-a" -> {
                parser.setSortingOrderAsc(true);
                return true;
            }
            case "-d" -> {
                parser.setSortingOrderAsc(false);
                return true;
            }
        }
        parser.setCurrentState(parser.getReadingOutputFileState());
        return parser.getReadingOutputFileState().parseArg(arg, parser);
    }

    @Override
    public CommandLineArgState nextState(CommandLineArgsParser parser) {
        return parser.getReadingInputFilesState();
    }
}
