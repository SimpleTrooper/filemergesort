package reader.lineparser;

import exception.LineFormatException;

public class StringParser implements LineParser<String> {
    @Override
    public String parse(String line) throws LineFormatException {
        if (line.contains("\s")) {
            throw new LineFormatException(String.format("Can't parse string : String contains space character: " +
                    "For input string \"%s\"", line));
        }
        return line;
    }
}