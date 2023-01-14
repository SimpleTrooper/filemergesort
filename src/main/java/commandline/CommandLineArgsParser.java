package commandline;

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

    public boolean parseArgs(String[] args) {
        readingFlagsState = new ReadingFlagsState();
        readingOutputFileState = new ReadingOutputFileState();
        readingInputFilesState = new ReadingInputFilesState();

        currentState = readingFlagsState;
        for (String arg: args) {
            currentState.parseArg(arg, this);
        }
        return checkArgs();
    }

    public void addInputFile(String inputFile) {
        inputFiles.add(inputFile);
    }

    private boolean checkArgs() {
        if (outputFile == null) {
            return false;
        }
        if (dataType == null) {
            return false;
        }
        if (inputFiles.size() == 0) {
            return false;
        }
        return true;
    }
}
