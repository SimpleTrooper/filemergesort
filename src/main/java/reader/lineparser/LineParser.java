package reader.lineparser;

import exception.LineFormatException;

/**
 * Интерфейс для обработки строк
 * @param <T> тип данных
 */
@FunctionalInterface
public interface LineParser<T> {
    T parse(String line) throws LineFormatException;
}
