import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NestJoin {
    public NestJoin() {

    }

    public void join() {
        long start = System.currentTimeMillis();
        Path pathA, pathB;
        byte[] dataA = new byte[4000];
        byte[] dataB = new byte[4000];
        int valueA = 0, valueB = 0, count = 0;
        for (int af = 1; af < 100; af++) {
            pathA = Paths.get("Project3Dataset-A/A" + af + ".txt");
            try {
                dataA = Files.readAllBytes(pathA);
                for (int ar = 0; ar < 100; ar++) {
                    valueA = 100 * (int) ((char) dataA[34 + 40 * ar] - 48) +
                            10 * (int) ((char) dataA[35 + 40 * ar] - 48) +
                            (int) ((char) dataA[36 + 40 * ar] - 48);

                    for (int bf = 1; bf < 100; bf++){
                        pathB = Paths.get("Project3Dataset-B/B" + bf + ".txt");
                        dataB = Files.readAllBytes(pathB);
                        for (int br = 0; br < 100; br++) {
                            valueB = 100 * (int) ((char) dataB[34 + 40 * br] - 48) +
                                    10 * (int) ((char) dataB[35 + 40 * br] - 48) +
                                    (int) ((char) dataB[36 + 40 * br] - 48);
                            if (valueA > valueB)
                                count++;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Count = " + count);
        System.out.println("Execution time is " + timeElapsed + " ms");
    }
}
