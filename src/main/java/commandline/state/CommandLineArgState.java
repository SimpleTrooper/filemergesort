package commandline.state;

import commandline.CommandLineArgsParser;

/**
 * Состояние обработчика командной строки
 */
public interface CommandLineArgState {
    boolean parseArg(String arg, CommandLineArgsParser parser);
}
