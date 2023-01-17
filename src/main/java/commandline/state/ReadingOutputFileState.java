package commandline.state;

import commandline.CommandLineArgsParser;

/**
 * Состояние - считывается выходной файл
 */
public class ReadingOutputFileState implements CommandLineArgState {
    @Override
    public boolean parseArg(String arg, CommandLineArgsParser parser) {
        parser.setOutputFile(arg);
        parser.setCurrentState(parser.getReadingInputFilesState());
        return true;
    }
}
