public interface LineParser<T> {
    T parse(String line) throws LineFormatException;
}
