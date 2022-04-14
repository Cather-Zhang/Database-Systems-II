import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArrayIndex {
    private String[] ptr;

    public ArrayIndex() {
        this.ptr = new String[5000];
    }

    public void put(int file, int recordNum, int randomV) {
        if (ptr[randomV - 1] == null)
            ptr[randomV - 1] = "";
        ptr[randomV - 1] = ptr[randomV - 1] + (char) file + (char) recordNum;
    }

    public void get(int randomV) {
        String str = ptr[randomV - 1];
        if (str == null)
            return;
        int file, recordNum;
        Path path;
        byte[] data = new byte[4000];
        byte[] out = new byte[40];
        String record;

        for (int i = 0; i < str.length() - 1; i += 2) {
            file = (int) str.charAt(i);
            recordNum = (int) str.charAt(i + 1);
            path = Paths.get("Project2Dataset/F" + file + ".txt");
            try {
                data = Files.readAllBytes(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int k = 0; k < 40; k++) {
                out[k] = data[k + recordNum * 40];
            }
            record = new String(out);
            System.out.println(record);
        }
    }

    public void range(int lower, int upper) {
        String str;
        int file, recordNum;
        Path path;
        byte[] data = new byte[4000];
        byte[] out = new byte[40];
        String record;
        int count = 0;

        for (int a = lower + 1; a < upper; a++) {
            str = ptr[a - 1];
            if (str == null)
                continue;
            for (int i = 0; i < str.length() - 1; i += 2) {
                file = (int) str.charAt(i);
                recordNum = (int) str.charAt(i + 1);
                path = Paths.get("Project2Dataset/F" + file + ".txt");
                try {
                    data = Files.readAllBytes(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int k = 0; k < 40; k++) {
                    out[k] = data[k + recordNum * 40];
                }
                record = new String(out);
                count++;
                System.out.println(record);
            }
        }
        System.out.println(count);
    }

    public void inequality(int randomV) {
        String str;
        int file, recordNum;
        Path path;
        byte[] data = new byte[4000];
        byte[] out = new byte[40];
        String record;
        int count = 0;

        for (int a = 1; a < 5001; a++) {
            if (randomV == a)
                continue;
            str = ptr[a - 1];
            if (str == null)
                continue;
            for (int i = 0; i < str.length() - 1; i += 2) {
                file = (int) str.charAt(i);
                recordNum = (int) str.charAt(i + 1);
                path = Paths.get("Project2Dataset/F" + file + ".txt");
                try {
                    data = Files.readAllBytes(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                 * for (int k = 0; k < 40; k++) {
                 * out[k] = data[k + recordNum * 40];
                 * }
                 * record = new String(out);
                 * System.out.println(record);
                 */
                count++;
            }
        }
        System.out.println(count);
    }
}
