import java.io.BufferedReader;
import java.io.IOException;

public class BufferedLongReader implements NextLineReader<Long>, AutoCloseable {
    private final BufferedReader reader;

    public BufferedLongReader(BufferedReader reader) {
        this.reader = reader;
    }

    public Long readLine() throws IOException, LineFormatException {
        String nextLine = reader.readLine();
        try {
            return Long.valueOf(nextLine);
        } catch (NumberFormatException ex) {
            throw new LineFormatException(ex.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }
}
