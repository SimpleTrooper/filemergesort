package commandline;

import commandline.state.CommandLineArgState;
import commandline.state.ReadingFlagsState;
import commandline.state.ReadingInputFilesState;
import commandline.state.ReadingOutputFileState;
import exception.CommandLineArgsException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommandLineArgsParser {
    private String outputFile;
    private final List<String> inputFiles = new ArrayList<>();
    private DataType dataType;
    private boolean sortingOrderAsc = true;

    private CommandLineArgState readingFlagsState;
    private CommandLineArgState readingOutputFileState;
    private CommandLineArgState readingInputFilesState;

    private CommandLineArgState currentState;

    public void parseArgs(String[] args) throws CommandLineArgsException {
        readingFlagsState = new ReadingFlagsState();
        readingOutputFileState = new ReadingOutputFileState();
        readingInputFilesState = new ReadingInputFilesState();

        currentState = readingFlagsState;
        for (String arg: args) {
            currentState.parseArg(arg, this);
        }
        checkArgs();
    }

    public void addInputFile(String inputFile) {
        inputFiles.add(inputFile);
    }

    private void checkArgs() throws CommandLineArgsException {
        if (outputFile == null) {
            throw new CommandLineArgsException("Output file is not specified");
        }
        if (dataType == null) {
            throw new CommandLineArgsException("Data type is not specified");
        }
        if (inputFiles.size() == 0) {
            throw new CommandLineArgsException("Input files is not specified");
        }
    }
}
