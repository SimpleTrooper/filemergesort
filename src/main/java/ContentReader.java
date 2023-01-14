import java.io.IOException;
import java.util.Queue;

public class ContentReader<T> {
    private static final int MAX_QUEUE_SIZE = 100;
    private final NextLineReader<T> nextLineReader;
    private final Queue<T> content;

    public ContentReader(NextLineReader<T> nextLineReader, Queue<T> content) {
        this.nextLineReader = nextLineReader;
        this.content = content;
    }

    public synchronized void readContent() throws IOException, LineFormatException {
        while (nextLineReader.ready()) {
            try {
                while (content.size() > MAX_QUEUE_SIZE) {
                    wait();
                }
                content.add(nextLineReader.readLine());
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }
}
