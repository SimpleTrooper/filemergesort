package reader;

import exception.LineFormatException;

import java.io.IOException;

public interface NextLineReader<T> {
    T readLine() throws IOException, LineFormatException;

    boolean ready() throws IOException;
}
