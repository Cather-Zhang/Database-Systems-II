import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FullTableScan {

    /**
     * full table scan, go into each file and each record to find value;
     * only use this function if no array is built
     * 
     * @param randomV
     */
    public void get(int randomV) {
        Path path;
        byte[] data = new byte[4000];
        int value = 0;
        byte[] out = new byte[40];
        String record;
        int count = 0;

        for (int i = 1; i < 100; i++) {
            path = Paths.get("Project2Dataset/F" + i + ".txt");
            try {
                data = Files.readAllBytes(path);
                for (int j = 0; j < 100; j++) {
                    value = 1000 * (int) ((char) data[33 + 40 * j] - 48) +
                            100 * (int) ((char) data[34 + 40 * j] - 48) +
                            10 * (int) ((char) data[35 + 40 * j] - 48) +
                            (int) ((char) data[36 + 40 * j] - 48);
                    if (value == randomV) {
                        for (int k = 0; k < 40; k++) {
                            out[k] = data[k + 40 * j];
                        }
                        record = new String(out);
                        count++;
                        System.out.println(record);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(count);
    }

    public void range(int lower, int upper) {
        Path path;
        byte[] data = new byte[4000];
        int value = 0;
        byte[] out = new byte[40];
        String record;
        int count = 0;

        for (int i = 1; i < 100; i++) {
            path = Paths.get("Project2Dataset/F" + i + ".txt");
            try {
                data = Files.readAllBytes(path);
                for (int j = 0; j < 100; j++) {
                    value = 1000 * (int) ((char) data[33 + 40 * j] - 48) +
                            100 * (int) ((char) data[34 + 40 * j] - 48) +
                            10 * (int) ((char) data[35 + 40 * j] - 48) +
                            (int) ((char) data[36 + 40 * j] - 48);
                    if (value > lower && value < upper) {
                        for (int k = 0; k < 40; k++) {
                            out[k] = data[k + 40 * j];
                        }
                        record = new String(out);
                        count++;
                        System.out.println(record);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(count);
    }

    public void inequality(int randomV) {
        Path path;
        byte[] data = new byte[4000];
        int value = 0;
        byte[] out = new byte[40];
        String record;
        int count = 0;

        for (int i = 1; i < 100; i++) {
            path = Paths.get("Project2Dataset/F" + i + ".txt");
            try {
                data = Files.readAllBytes(path);
                for (int j = 0; j < 100; j++) {
                    value = 1000 * (int) ((char) data[33 + 40 * j] - 48) +
                            100 * (int) ((char) data[34 + 40 * j] - 48) +
                            10 * (int) ((char) data[35 + 40 * j] - 48) +
                            (int) ((char) data[36 + 40 * j] - 48);
                    if (value != randomV) {
                        /*
                         * for (int k = 0; k < 40; k++) {
                         * out[k] = data[k + 40 * j];
                         * }
                         * record = new String(out);
                         * System.out.println(record);
                         */
                        count++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(count);
    }

}
