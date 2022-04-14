import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        HashIndex hi = new HashIndex();
        ArrayIndex ai = new ArrayIndex();
        FullTableScan fts = new FullTableScan();
        buildArray(hi, ai);

        System.out.println("equity search:");
        System.out.println("Full Table Scan:");
        fts.get(10);
        System.out.println("Hash index:");
        hi.get(10);
        System.out.println("Array index:");
        ai.get(10);

        /*
         * System.out.println("range search:");
         * System.out.println("Full Table Scan:");
         * fts.range(21, 40);
         * System.out.println("Hash index:");
         * hi.range(21, 40);
         * System.out.println("Array index:");
         * ai.range(21, 40);
         */
        System.out.println("inequality search:");
        System.out.println("Full Table Scan:");
        fts.inequality(10);
        System.out.println("Hash index:");
        hi.inequality(10);
        System.out.println("Array index:");
        ai.inequality(10);
    }

    public static void buildArray(HashIndex hi, ArrayIndex ai) {
        Path path;
        byte[] data = new byte[4000];
        int value = 0;

        for (int i = 1; i < 100; i++) {
            path = Paths.get("Project2Dataset/F" + i + ".txt");
            try {
                data = Files.readAllBytes(path);
                for (int j = 0; j < 100; j++) {
                    value = 1000 * (int) ((char) data[33 + 40 * j] - 48) +
                            100 * (int) ((char) data[34 + 40 * j] - 48) +
                            10 * (int) ((char) data[35 + 40 * j] - 48) +
                            (int) ((char) data[36 + 40 * j] - 48);
                    hi.put(i, j, value);
                    ai.put(i, j, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
