import commandline.CommandLineArgsParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FileMergeSortLongType {
    private final CommandLineArgsParser commandLineArgsParser;
    private final Map<String, Queue<Long>> filesContent = new HashMap<>();
    private final Map<String, Thread> threadMap = new HashMap<>();

    public FileMergeSortLongType(CommandLineArgsParser commandLineArgsParser) {
        this.commandLineArgsParser = commandLineArgsParser;
    }

    public void readFiles() {
        for (String file : commandLineArgsParser.getInputFiles()) {
            Thread readFile = new Thread(() -> {
                Queue<Long> fileContent = new LinkedList<>();
                filesContent.put(file, fileContent);
                try (BufferedLineReader<Long> nextLineReader = new BufferedLineReader<>(
                        new BufferedReader(new FileReader(file)), new LongParser())) {
                    ContentReader<Long> fileContentReader = new ContentReader<>(nextLineReader, fileContent);
                    fileContentReader.readContent();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (LineFormatException lineFormatException) {
                    lineFormatException.printStackTrace();
                }
            });
            threadMap.put(file, readFile);
            readFile.start();
        }
    }

    public void mergeAndWrite() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(commandLineArgsParser.getOutputFile()))) {
            boolean allFilesProcessed;
            do {
                allFilesProcessed = true;
                String nextFileWithMinimum = null;
                Long nextMinimum = Long.MAX_VALUE;
                for (Map.Entry<String, Queue<Long>> entry : filesContent.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        Thread fileThread = threadMap.get(entry.getKey());
                        try {
                            if (fileThread.isAlive()) {
                                fileThread.join(400L);
                            }
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        if (entry.getValue().isEmpty()) {
                            continue;
                        }
                    }
                    allFilesProcessed = false;
                    if (entry.getValue().peek() < nextMinimum) {
                        nextFileWithMinimum = entry.getKey();
                    }
                }
                if (filesContent.containsKey(nextFileWithMinimum)) {
                    writer.write(filesContent.get(nextFileWithMinimum).poll() + System.lineSeparator());
                }
            } while (!allFilesProcessed);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
