import commandline.CommandLineArgsParser;

public class FileMergeSortApp {

    public static void main(String[] args) {
        CommandLineArgsParser commandLineArgsParser = new CommandLineArgsParser();
        if (!commandLineArgsParser.parseArgs(args)) {
            System.out.println("Command line args is not valid!");
            return;
        }

        FileMergeSortLongType fileMergeSort = new FileMergeSortLongType(commandLineArgsParser);
        fileMergeSort.readFiles();
        fileMergeSort.mergeAndWrite();
    }
}
