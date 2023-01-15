import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMergeSortTest {
    public static void main(String[] args) {
        Path currentRelativePath = Paths.get("tests/1.txt");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s);
        generateFile();
    }

    static void generateFile() {
        long numberOfLongs = 100000000L;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("F:/filemergesort_tests/4.txt"))) {
            for (int i = 0; i < numberOfLongs; i++) {
                writer.write(i + System.lineSeparator());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
