package reader;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedLineReader<T> implements NextLineReader<T>, AutoCloseable {
    private final BufferedReader reader;
    private final LineParser<T> lineParser;

    public BufferedLineReader(BufferedReader reader, LineParser<T> lineParser) {
        this.reader = reader;
        this.lineParser = lineParser;
    }

    public T readLine() throws IOException, LineFormatException {
        String nextLine = reader.readLine();
        return lineParser.parse(nextLine);
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
