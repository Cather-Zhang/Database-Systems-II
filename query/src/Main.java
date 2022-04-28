import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        boolean ACTIVE = true;
        HashJoin hj = new HashJoin();
        NestJoin nj = new NestJoin();

        while (ACTIVE) {
            System.out.print(">> ");
            String command = s.nextLine();
            String com1 = "SELECT A.Col1, A.Col2, B.Col1, B.Col2 FROM A, B WHERE A.RandomV = B.RandomV";
            String com2 = "SELECT count(*) FROM A, B WHERE A.RandomV > B.RandomV";
            if (command.equals(com1)) {
                hj.join();
            }
            else if (command.equals(com2)) {
                nj.join();
            }
            else if (command.equals("exit")) {
                ACTIVE = false;
            }
            else {

            }
        }
        s.close();
    }

}
