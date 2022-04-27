import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

public class HashJoin {
    private Hashtable<Integer, ArrayList<ArrayList<String>>> ht;

    public HashJoin() {
        this.ht = new Hashtable<Integer, ArrayList<ArrayList<String>>>();
        this.buildTable();
    }

    public Hashtable<Integer, ArrayList<ArrayList<String>>> getTable() {
        return this.ht;
    }

    public void put(String col1, String col2, int randomV) {
        ArrayList<ArrayList<String>> cols = this.ht.get(randomV);
        ArrayList<String> newCols = new ArrayList<>();
        newCols.add(col1);
        newCols.add(col2);
        if (cols != null)
            cols.add(newCols);
        else {
            cols = new ArrayList<ArrayList<String>>();
            cols.add(newCols);
        }
        this.ht.put(randomV, cols);
    }

    public void buildTable() {
        Path path;
        byte[] data = new byte[4000];
        int value = 0;

        for (int i = 1; i < 100; i++) {
            path = Paths.get("Project3Dataset-A/A" + i + ".txt");
            try {
                data = Files.readAllBytes(path);
                byte[] col1b = new byte[10];
                byte[] col2b = new byte[7];
                for (int j = 0; j < 100; j++) {
                    value = 100 * (int) ((char) data[34 + 40 * j] - 48) +
                            10 * (int) ((char) data[35 + 40 * j] - 48) +
                            (int) ((char) data[36 + 40 * j] - 48);
                    
                    for (int k = 0; k < 19; k++) {
                        if (k < 10)
                            col1b[k] = data[k + j * 40];
                        if (k > 11)
                            col2b[k-12] = data[k + j * 40];
                    }
                    
                    String col1 = new String(col1b);
                    String col2 = new String(col2b);
                    this.put(col1, col2, value);
                   
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void join() {
        long start = System.currentTimeMillis();
        int count = 0;
        System.out.println("A.Col1        A.Col2      B.Col1        B.Col2");
        Path path;
        byte[] data = new byte[4000];
        int value = 0;

        for (int i = 1; i < 100; i++) {
            path = Paths.get("Project3Dataset-B/B" + i + ".txt");
            try {
                data = Files.readAllBytes(path);
                byte[] col1b = new byte[10];
                byte[] col2b = new byte[7];
                for (int j = 0; j < 100; j++) {
                    value = 100 * (int) ((char) data[34 + 40 * j] - 48) +
                            10 * (int) ((char) data[35 + 40 * j] - 48) +
                            (int) ((char) data[36 + 40 * j] - 48);
                    
                    for (int k = 0; k < 19; k++) {
                        if (k < 10)
                            col1b[k] = data[k + j * 40];
                        if (k > 11)
                            col2b[k-12] = data[k + j * 40];
                    }
                    
                    String col1 = new String(col1b);
                    String col2 = new String(col2b);
                    
                    ArrayList<ArrayList<String>> list = this.ht.get(value);

                    for (int k = 0; k < list.size(); k++) {
                        System.out.print(list.get(k).get(0) + "    " + list.get(k).get(1) + "     ");
                        System.out.println(col1 + "    " + col2);
                        count++;
                    }
                   
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Execution time is " + timeElapsed + " ms");
        System.out.println("There are total " + count + " outputs");
    }

}
