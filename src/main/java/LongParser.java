public class LongParser implements LineParser<Long> {
    @Override
    public Long parse(String line) throws LineFormatException {
        try {
            return Long.valueOf(line);
        } catch (NumberFormatException ex) {
            throw new LineFormatException(ex.getMessage());
        }
    }
}
