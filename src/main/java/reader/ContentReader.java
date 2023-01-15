package reader;

import java.io.IOException;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class ContentReader<T> {
    private static final int MAX_QUEUE_SIZE = 100000;
    private static final long THREAD_MAX_WAITING_TIME = 1000L;
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
               /* while (content.size() > MAX_QUEUE_SIZE) {
                    System.out.println("Queue is full. Waiting...");
                    wait(THREAD_MAX_WAITING_TIME);
                }*/
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
                interruptedException.printStackTrace();
            }
        }
    }
}
