import commandline.CommandLineArgsParser;
import exception.CommandLineArgsException;
import lombok.extern.slf4j.Slf4j;
import reader.lineparser.LongParser;
import reader.lineparser.StringParser;

import java.util.Comparator;

@Slf4j
public class FileMergeSortApp {

    public static void main(String[] args) {
        CommandLineArgsParser commandLineArgsParser = new CommandLineArgsParser();
        try {
            commandLineArgsParser.parseArgs(args);
        } catch (CommandLineArgsException commandLineArgsException) {
            log.error(commandLineArgsException.getMessage());
            return;
        }

        FileMergeSort<?> fileMergeSort = null;

        switch (commandLineArgsParser.getDataType()) {
            case INTEGER -> fileMergeSort = new FileMergeSort<>(commandLineArgsParser.getInputFiles(),
                    commandLineArgsParser.getOutputFile(), new LongParser(), Comparator.naturalOrder());
            case STRING -> fileMergeSort = new FileMergeSort<>(commandLineArgsParser.getInputFiles(),
                    commandLineArgsParser.getOutputFile(), new StringParser(), Comparator.naturalOrder());
        }
        if (fileMergeSort == null) {
            log.error("Application initialization error: FileMergeSort instance is null");
            return;
        }

        if (!commandLineArgsParser.isSortingOrderAsc()) {
            fileMergeSort.reverseSortingOrder();
        }

        fileMergeSort.mergeFiles();
    }
}
