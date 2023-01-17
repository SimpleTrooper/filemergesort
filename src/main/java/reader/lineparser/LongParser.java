package reader.lineparser;

import exception.LineFormatException;

/**
 * Обработчик Long
 */
public class LongParser implements LineParser<Long> {
    @Override
    public Long parse(String line) throws LineFormatException {
        try {
            return Long.valueOf(line);
        } catch (NumberFormatException ex) {
            throw new LineFormatException(String.format("Cant's parse long value: %s", ex.getMessage()));
        }
    }
}
