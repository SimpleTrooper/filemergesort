package exception;

/**
 * Исключение - ошибка при обработке командной строки
 */
public class CommandLineArgsException extends Exception {
    public CommandLineArgsException(final String message) {
        super(message);
    }
}
