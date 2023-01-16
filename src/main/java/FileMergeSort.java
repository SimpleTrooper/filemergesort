import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reader.BufferedLineReader;
import reader.ContentReader;
import exception.LineFormatException;
import reader.lineparser.LineParser;
import exception.SortingOrderException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@Setter
public class FileMergeSort<T> {
    private final static long THREAD_MAX_WAITING_TIME = 400L;
    private final static int MAX_QUEUE_SIZE = 1000;
    private final Map<String, BlockingQueue<T>> filesContent = new ConcurrentHashMap<>();
    private final Map<String, Thread> threadMap = new ConcurrentHashMap<>();
    private final List<String> inputFiles;
    private final String outputFile;
    private LineParser<T> lineParser;
    private Comparator<T> lineComparator;

    public FileMergeSort(List<String> inputFiles, String outputFile,
                         LineParser<T> lineParser, Comparator<T> lineComparator) {
        this.inputFiles = inputFiles;
        this.outputFile = outputFile;
        this.lineParser = lineParser;
        this.lineComparator = lineComparator;
    }

    public void reverseSortingOrder() {
        lineComparator = lineComparator.reversed();
    }

    public void mergeFiles() {
        startReadingThreads();
        mergeAndWrite();
    }

    private void startReadingThreads() {
        for (String file : inputFiles) {
            BlockingQueue<T> fileContent = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
            filesContent.put(file, fileContent);
            Thread readFile = new Thread(() -> {
                try (BufferedLineReader<T> nextLineReader = new BufferedLineReader<>(
                        new BufferedReader(new FileReader(file)), lineParser)) {
                    ContentReader<T> fileContentReader = new ContentReader<>(nextLineReader, fileContent,
                            lineComparator);
                    fileContentReader.readContent();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SortingOrderException | LineFormatException sortingOrderException) {
                    log.error("File: {}: {}. Following lines will not be processed for that file!", file, sortingOrderException.getMessage());
                } catch (OutOfMemoryError outOfMemoryError) {
                    log.error("OutOfMemory: File: {}: {}", file, outOfMemoryError.getMessage());
                }
            });
            threadMap.put(file, readFile);
            readFile.start();
        }
    }

    private void mergeAndWrite() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            boolean allFilesProcessed;
            do {
                allFilesProcessed = true;
                String nextFileWithMinimum = null;
                T nextMinimum = null;

                for (Map.Entry<String, BlockingQueue<T>> entry : filesContent.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        Thread fileThread = threadMap.get(entry.getKey());
                        try {
                            if (fileThread.isAlive()) {
                                fileThread.join(THREAD_MAX_WAITING_TIME);
                            }
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        if (entry.getValue().isEmpty()) {
                            continue;
                        }
                    }
                    allFilesProcessed = false;
                    T nextValue = entry.getValue().peek();
                    if (nextMinimum == null || lineComparator.compare(nextValue, nextMinimum) < 0) {
                        nextFileWithMinimum = entry.getKey();
                        nextMinimum = nextValue;
                    }
                }
                if (nextFileWithMinimum != null && filesContent.containsKey(nextFileWithMinimum)) {
                    writer.write(filesContent.get(nextFileWithMinimum).poll() + System.lineSeparator());
                }
            } while (!allFilesProcessed);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
