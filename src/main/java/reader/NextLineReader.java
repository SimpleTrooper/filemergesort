package reader;

import exception.LineFormatException;

import java.io.IOException;

/**
 * Интерфейс построчного чтения данных
 * @param <T> тип данных
 */
public interface NextLineReader<T> {
    T readLine() throws IOException, LineFormatException;

    boolean ready() throws IOException;
}
