package reader.lineparser;

import exception.LineFormatException;

@FunctionalInterface
public interface LineParser<T> {
    T parse(String line) throws LineFormatException;
}
