package reader;

import exception.LineFormatException;
import exception.SortingOrderException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;

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

    public void readContent() throws IOException, LineFormatException, SortingOrderException {
        long lineNumber = 0;
        while (nextLineReader.ready()) {
            try {
                T nextLine = nextLineReader.readLine();
                if (previousLine != null && lineComparator.compare(nextLine, previousLine) < 0) {
                    throw new SortingOrderException(String.format("Line %d: Sorting order violation", lineNumber));
                }
                content.put(nextLine);
                previousLine = nextLine;
                lineNumber++;
            } catch (LineFormatException lineFormatException) {
                throw new LineFormatException(String.format("Line %d: %s", lineNumber,
                        lineFormatException.getMessage()));
            } catch (InterruptedException interruptedException) {
                log.error("Interrupted exception: " + interruptedException.getMessage());
            }  catch (OutOfMemoryError outOfMemoryError) {
                throw new OutOfMemoryError(String.format("Line %d is to large for heap", lineNumber));
            }
        }
    }
}
