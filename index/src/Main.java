import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        FullTableScan fts = new FullTableScan();
        HashIndex hi = new HashIndex();
        ArrayIndex ai = new ArrayIndex();
        boolean ACTIVE = true;
        boolean index = false;
        while (ACTIVE) {
            System.out.println("Program is ready and waiting for user command.");
            System.out.print(">> ");
            String[] command = s.nextLine().split("\\ ", 12);
            int query = -1;
            if (command[0].equals("exit"))
                return;
            switch (command.length) {
                case (1):
                    query = -1; // exit
                case (5):
                    query = 0; // create index
                    index = true;
                    break;
                case (8):
                    if (command[6].equals("="))
                        query = 1; // equality search
                    else
                        query = 3; // inequality search
                    break;
                case (12):
                    query = 2; // range search
                    break;
                default:
                    continue;
            }
            switch (query) {
                case (-1):
                    ACTIVE = false;
                    break;
                case (0):
                    buildArray(hi, ai);
                    System.out.println("The hash-based and array-based indexes are built successfully.");
                    break;
                case (1):
                    if (index) {
                        hi.get(Integer.parseInt(command[7]));
                    } else {
                        fts.get(Integer.parseInt(command[7]));
                    }
                    break;
                case (2):
                    if (index) {
                        ai.range(Integer.parseInt(command[7]), Integer.parseInt(command[11]));
                    } else {
                        fts.range(Integer.parseInt(command[7]), Integer.parseInt(command[11]));
                    }
                    break;
                case (3):
                    fts.inequality(Integer.parseInt(command[7]));

            }
        }
        s.close();

        /*
         * System.out.println("equity search:");
         * System.out.println("Full Table Scan:");
         * fts.get(1000);
         * System.out.println("Hash index:");
         * hi.get(1000);
         * System.out.println("Array index:");
         * ai.get(1000);
         * 
         * 
         * System.out.println("range search:");
         * System.out.println("Full Table Scan:");
         * fts.range(21, 400);
         * System.out.println();
         * System.out.println("Hash index:");
         * hi.range(21, 400);
         * System.out.println();
         * System.out.println("Array index:");
         * ai.range(21, 400);
         * 
         * System.out.println("inequality search:");
         * System.out.println("Full Table Scan:");
         * fts.inequality(100);
         * System.out.println();
         * System.out.println("Hash index:");
         * hi.inequality(100);
         * System.out.println();
         * System.out.println("Array index:");
         * ai.inequality(100);
         */
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
