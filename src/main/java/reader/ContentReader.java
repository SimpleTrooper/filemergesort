package reader;

import exception.LineFormatException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;

/**
 * Класс для построчного чтения данных из NextLineReader в BlockingQueue
 * @param <T> тип данных
 */
@Slf4j
public class ContentReader<T> {
    private final NextLineReader<T> nextLineReader;
    private final BlockingQueue<T> content;
    private final Comparator<T> lineComparator;
    private T previousLine;

    public ContentReader(NextLineReader<T> nextLineReader, BlockingQueue<T> content, Comparator<T> lineComparator) {
        this.nextLineReader = nextLineReader;
        this.content = content;
        this.lineComparator = lineComparator;
    }

    public void readContent() throws IOException {
        long lineNumber = 0;
        while (nextLineReader.ready()) {
            try {
                T nextLine = nextLineReader.readLine();
                if (previousLine != null && lineComparator.compare(nextLine, previousLine) < 0) {
                    log.error("Line {} is skipped: Sorting order violation", lineNumber);
                    lineNumber++;
                    continue;
                }
                content.put(nextLine);
                previousLine = nextLine;
                lineNumber++;
            } catch (LineFormatException lineFormatException) {
                log.error("Line {} is skipped: {}", lineNumber, lineFormatException.getMessage());
            } catch (InterruptedException interruptedException) {
                log.error("Interrupted exception: ", interruptedException);
            }  catch (OutOfMemoryError outOfMemoryError) {
                throw new OutOfMemoryError(String.format("Line %d is to large for heap", lineNumber));
            }
        }
    }
}
