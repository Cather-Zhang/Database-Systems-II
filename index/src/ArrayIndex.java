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
        long start = System.currentTimeMillis();
        String str = ptr[randomV - 1];
        if (str == null) {
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("The number of records whose RandomV are equal to " + randomV + " is 0");
            System.out.println("The index type used is Hash Index");
            System.out.println("Execution time is " + timeElapsed + " ms");
            System.out.println("The program needed to read 0 block");
            return;
        }
        int file, recordNum, count = 0, block = 0;
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
                block++;
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
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("The number of records whose RandomV are equal to " + randomV + " is " + count);
        System.out.println("The index type used is Array Index");
        System.out.println("Execution time is " + timeElapsed + " ms");
        System.out.println("The program needed to read " + block + " blocks");
    }

    public void range(int lower, int upper) {
        long start = System.currentTimeMillis();
        String str;
        int file, recordNum;
        Path path;
        byte[] data = new byte[4000];
        byte[] out = new byte[40];
        String record;
        int count = 0, block = 0;

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
                    block++;
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
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("The number of records whose RandomV are > " + lower + " and < " + upper + " is " + count);
        System.out.println("The index type used is Array Index");
        System.out.println("Execution time is " + timeElapsed + " ms");
        System.out.println("The program needed to read " + block + " blocks");
    }

    public void inequality(int randomV) {
        long start = System.currentTimeMillis();
        String str;
        int file, recordNum;
        Path path;
        byte[] data = new byte[4000];
        byte[] out = new byte[40];
        String record;
        int count = 0, block = 0;

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
                    block++;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int k = 0; k < 40; k++) {
                    out[k] = data[k + recordNum * 40];
                }
                record = new String(out);
                System.out.println(record);

                count++;
            }
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("The number of records whose RandomV are not equal to " + randomV + " is " + count);
        System.out.println("The index type used is Array Index");
        System.out.println("Execution time is " + timeElapsed + " ms");
        System.out.println("The program needed to read " + block + " blocks");
    }
}
