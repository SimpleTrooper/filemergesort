import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import exception.LineFormatException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reader.lineparser.LineParser;
import reader.lineparser.LongParser;
import reader.lineparser.StringParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестовый класс для FileMergeSort. Использует дисковое пространство в папке "src/test/resources"
 */
@Slf4j
public class FileMergeSortTest {
    private final String resourcesFolder = "src/test/resources/";
    private List<String> inputFiles;
    private String outputFile;

    @Test
    void shouldSortIntegersForCustomTestCase1Asc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt");
        outputFile = resourcesFolder + "out.txt";
        FileMergeSort<Long> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new LongParser(),
                Comparator.naturalOrder());

        List<List<Long>> valuesForFiles = List.of(List.of(1L, 4L, 9L), List.of(1L, 8L, 27L),
                List.of(1L, 2L, 3L));
        List<Long> expected = List.of(1L, 1L, 1L, 2L, 3L, 4L, 8L, 9L, 27L);
        writeValuesToFiles(valuesForFiles, inputFiles);

        fileMergeSort.mergeFiles();

        List<Long> actual = readValuesFromFile(outputFile, new LongParser());

        assertEquals(expected, actual);
    }

    @Test
    void shouldSortIntegersForCustomTestCase2PartialDesc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        FileMergeSort<Long> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new LongParser(),
                Comparator.naturalOrder());
        fileMergeSort.reverseSortingOrder();

        List<List<Long>> valuesForFiles = List.of(List.of(10L, 9L, 1L), List.of(1L, 1L, 1L),
                List.of(2L, 1L, 3L), List.of(10L, 8L, 5L));
        List<Long> expected = List.of(10L, 10L, 9L, 8L, 5L, 2L, 1L, 1L, 1L, 1L, 1L);
        writeValuesToFiles(valuesForFiles, inputFiles);

        fileMergeSort.mergeFiles();

        List<Long> actual = readValuesFromFile(outputFile, new LongParser());

        assertEquals(expected, actual);
    }

    @Test
    void shouldSortStringsForCustomTestCase1Asc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        FileMergeSort<String> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new StringParser(),
                Comparator.naturalOrder());

        List<List<String>> valuesForFiles = List.of(List.of("A", "B", "C"), List.of("AA", "BB", "CC"),
                List.of("AAA", "BBB", "CCC"), List.of("a", "b", "c"));
        List<String> expected = List.of("A", "AA", "AAA", "B", "BB", "BBB", "C", "CC", "CCC", "a", "b", "c");
        writeValuesToFiles(valuesForFiles, inputFiles);

        fileMergeSort.mergeFiles();

        List<String> actual = readValuesFromFile(outputFile, new StringParser());

        assertEquals(expected, actual);
    }

    @Test
    void shouldSortStringsForCustomTestCase2PartialAsc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        FileMergeSort<String> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new StringParser(),
                Comparator.naturalOrder());

        List<List<String>> valuesForFiles = List.of(List.of("A", "B", "A"), List.of("AA", "BB ", "CC"),
                List.of("AA A", "BBB", "CCC"), List.of("z", "b", "c"));
        List<String> expected = List.of("A", "AA", "B", "z");
        writeValuesToFiles(valuesForFiles, inputFiles);

        fileMergeSort.mergeFiles();

        List<String> actual = readValuesFromFile(outputFile, new StringParser());

        assertEquals(expected, actual);
    }

    @Test
    void shouldSortStringsForCustomTestCase3PartialDesc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        FileMergeSort<String> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new StringParser(),
                Comparator.naturalOrder());
        fileMergeSort.reverseSortingOrder();

        List<List<String>> valuesForFiles = List.of(List.of("B", "B", "A"), List.of("BB", "AA", "CC"),
                List.of("BBB", "BB B ", "AAA"), List.of("z", "c", "b"));
        List<String> expected = List.of("z", "c", "b", "BBB", "BB", "B", "B", "AA", "A");
        writeValuesToFiles(valuesForFiles, inputFiles);

        fileMergeSort.mergeFiles();

        List<String> actual = readValuesFromFile(outputFile, new StringParser());

        assertEquals(expected, actual);
    }

    @Test
    void shouldSortLargeLongFilesAsc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        int size = 100000;

        for (String inputFile: inputFiles) {
            generateLongFileAsc(inputFile, size);
        }
        FileMergeSort<Long> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new LongParser(),
                Comparator.naturalOrder());

        fileMergeSort.mergeFiles();

        int inputFilesSize = inputFiles.size();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < inputFilesSize; j++) {
                    if (reader.ready()) {
                        assertEquals(i, Long.valueOf(reader.readLine()));
                    }
                }
            }
        } catch (IOException ioException) {
            log.error("Exception while reading output file: ", ioException);
        }
    }

    @Test
    void shouldSortLargeLongFilesDesc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        int size = 100000;

        for (String inputFile: inputFiles) {
            generateLongFileDesc(inputFile, size);
        }
        FileMergeSort<Long> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new LongParser(),
                Comparator.naturalOrder());

        fileMergeSort.mergeFiles();

        int inputFilesSize = inputFiles.size();
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            for (int i = size; i >= 0; i--) {
                for (int j = 0; j < inputFilesSize; j++) {
                    if (reader.ready()) {
                        assertEquals(i, Long.valueOf(reader.readLine()));
                    }
                }
            }
        } catch (IOException ioException) {
            log.error("Exception while reading output file: ", ioException);
        }
    }

    @Test
    void shouldSortLargeStringFilesAsc() {
        inputFiles = List.of(resourcesFolder + "in1.txt", resourcesFolder + "in2.txt",
                resourcesFolder + "in3.txt", resourcesFolder + "in4.txt");
        outputFile = resourcesFolder + "out.txt";
        int size = 10000;

        for (String inputFile: inputFiles) {
            generateStringFileAsc(inputFile, size);
        }
        FileMergeSort<String> fileMergeSort = new FileMergeSort<>(inputFiles, outputFile, new StringParser(),
                Comparator.naturalOrder());

        fileMergeSort.mergeFiles();

        int inputFilesSize = inputFiles.size();
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                if (i < size / 3) {
                    builder.append('a');
                } else if (i < (size * 2) / 3) {
                    builder.append('b');
                } else {
                    builder.append('c');
                }
                for (int j = 0; j < inputFilesSize; j++) {
                    if (reader.ready()) {
                        assertEquals(builder.toString(), reader.readLine());
                    }
                }
            }
        } catch (IOException ioException) {
            log.error("Exception while reading output file: ", ioException);
        }
    }

    @AfterEach
    public void clean() {
        try {
            for (String inputFile: inputFiles) {
                Files.deleteIfExists(Paths.get(inputFile));
            }
            Files.deleteIfExists(Paths.get(outputFile));
        } catch (IOException ioException) {
            log.error("Exception while deleting files: ", ioException);
        }
        inputFiles = new ArrayList<>();
        outputFile = null;
    }

    private <T> void writeValuesToFiles(List<List<T>> values, List<String> files) {
        Iterator<List<T>> valuesIterator = values.iterator();

        for (String inputFile: files) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
                if (valuesIterator.hasNext()) {
                    List<T> valuesForFile = valuesIterator.next();
                    for (T value: valuesForFile) {
                        writer.write(value + System.lineSeparator());
                    }
                }
            } catch (IOException ioException) {
                log.error("Exception while writing to files: ", ioException);
            }
        }
    }

    private <T> List<T> readValuesFromFile(String file, LineParser<T> lineParser) {
        List<T> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                values.add(lineParser.parse(reader.readLine()));
            }
        } catch (IOException | NumberFormatException | LineFormatException exception) {
            log.error("Exception while reading file: ", exception);
        }
        return values;
    }

    private static void generateLongFileAsc(String filename, int numberOfIntegers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < numberOfIntegers; i++) {
                writer.write(i + System.lineSeparator());
            }
        } catch (IOException ioException) {
            log.error("Exception while generating file with int values ascending order: ", ioException);
        }
    }

    private static void generateLongFileDesc(String filename, int numberOfIntegers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = numberOfIntegers; i >= 0; i--) {
                writer.write(i + System.lineSeparator());
            }
        } catch (IOException ioException) {
            log.error("Exception while generating file with int values descending order: ", ioException);
        }
    }

    private static void generateStringFileAsc(String filename, int numberOfStrings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < numberOfStrings; i++) {
                if (i < numberOfStrings / 3) {
                    builder.append('a');
                } else if (i < (numberOfStrings * 2) / 3) {
                    builder.append('b');
                } else {
                    builder.append('c');
                }
                writer.write(builder + System.lineSeparator());
            }
        } catch (IOException ioException) {
            log.error("Exception while generating file with string values ascending order: ", ioException);
        }
    }
}
