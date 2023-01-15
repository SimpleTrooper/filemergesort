import commandline.CommandLineArgsParser;
import reader.LineParser;
import reader.LongParser;
import reader.StringParser;

import java.util.Comparator;

public class FileMergeSortApp {

    public static void main(String[] args) {
        CommandLineArgsParser commandLineArgsParser = new CommandLineArgsParser();
        if (!commandLineArgsParser.parseArgs(args)) {
            System.out.println("Command line args is not valid!");
            return;
        }

        FileMergeSort<?> fileMergeSort;
        LineParser lineParser = new LongParser();
        Comparator<?> lineComparator = Comparator.naturalOrder();

        switch (commandLineArgsParser.getDataType()) {
            case INTEGER -> {
                lineParser = new LongParser();
            }
            case STRING -> lineParser = new StringParser();
        }

        if (!commandLineArgsParser.isSortingOrderAsc()) {
            lineComparator = lineComparator.reversed();
        }

        fileMergeSort = new FileMergeSort<>(commandLineArgsParser.getInputFiles(),
                commandLineArgsParser.getOutputFile(), lineParser, lineComparator);
        fileMergeSort.readFiles();
        fileMergeSort.mergeAndWrite();
    }

}
