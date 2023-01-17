package exception;

/**
 * Исключение - неверный формат очередной строки при чтении
 */
public class LineFormatException extends Exception {
    public LineFormatException(final String message) {
        super(message);
    }
}
