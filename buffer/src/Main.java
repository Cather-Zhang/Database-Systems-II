import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferPool bp = new BufferPool();
        /*
         * bp.init(3);
         * 
         * bp.set(430, "F05-Rec450, Jane Do, 10 Hill Rd, age020.");
         * System.out.
         * println("Output: Write was successful; Brought File 5 from disk; Placed in Frame 1\n"
         * );
         * bp.get(430);
         * System.out.println(
         * "Output: F05-Rec450, Jane Do, 10 Hill Rd, age020.; File 5 already in memory; Located in Frame 1\n"
         * );
         * bp.get(20);
         * System.out.println(
         * "Output: F01-Rec020, Name020, address020, age020.; Brought file 1 from disk; Placed in Frame 2\n"
         * );
         * bp.set(430, "F05-Rec450, John Do, 23 Lake Ln, age056.");
         * System.out.
         * println("Output: Write was successful; File 5 already in memory; Located in Frame 1\n"
         * );
         * bp.pin(5);
         * System.out.println("Output: File 5 pinned in Frame 1; Not already pinned\n");
         * bp.unpin(3);
         * System.out.
         * println("Output: The corresponding block 3 cannot be unpinned because it is not in memory.\n"
         * );
         * bp.get(430);
         * System.out.println(
         * "Output: F05-Rec450, John Do, 23 Lake Ln, age056.; File 5 already in memory; Located in Frame 1\n"
         * );
         * bp.pin(5);
         * System.out.println("Output: File 5 pinned in Frame 1; Already pinned\n");
         * bp.get(646);
         * System.out.println(
         * "Output: F07-Rec646, Name646, address646, age646.; Brought file 7 from disk; Placed in Frame 3\n"
         * );
         * bp.pin(3);
         * System.out.
         * println("Output: File 3 pinned in Frame 2; Not already pinned; Evicted file 1 from Frame 2\n"
         * );
         * bp.set(10, "F01-Rec010, Tim Boe, 09 Deer Dr, age009.");
         * System.out.println(
         * "Output: Write was successful; Brought File 1 from disk; Placed in Frame 3; Evicted file 7 from Frame 3\n"
         * );
         * bp.unpin(1);
         * System.out.
         * println("Output: File 1 in frame 3 is unpinned; Frame was already unpinned\n"
         * );
         * bp.get(355);
         * System.out.println(
         * "Output: F04-Rec355, Name355, address355, age355.; Brought file 4 from disk; Placed in Frame 3; Evicted file 1 from frame 3\n"
         * );
         * bp.pin(2);
         * System.out.println(
         * "Output: File 2 is pinned in Frame 3; Frame 3 was not already pinned; Evicted file 4 from frame 3\n"
         * );
         * bp.get(156);
         * System.out.println(
         * "Output: F02-Rec156, Name156, address156, age156.; File 2 already in memory; Located in Frame 3\n"
         * );
         * bp.set(10, "F01-Rec010, No Work, 31 Hill St, age100.");
         * System.out.println(
         * "Output: The corresponding block #1 cannot be  accessed from disk because the memory buffers are full; Write was unsuccessful\n"
         * );
         * bp.pin(7);
         * System.out.
         * println("Output: The corresponding block 7 cannot be pinned because the memory buffers are full\n"
         * );
         * bp.get(10);
         * System.out.println(
         * "Output: The corresponding block #1 cannot be accessed from disk because the memory buffers are full\n"
         * );
         * bp.unpin(3);
         * System.out.
         * println("Output: File 3 is unpinned in frame 2; Frame 2 was not already unpinned\n"
         * );
         * bp.unpin(2);
         * System.out.
         * println("Output: File 2 is unpinned in frame 3; Frame 3 was not already unpinned\n"
         * );
         * bp.get(10);
         * System.out.println(
         * "Output: F01-Rec010, Tim Boe, 09 Deer Dr, age009.; Brought file 1 from disk; Placed in Frame 2; Evicted file 3 from frame 2\n"
         * );
         * bp.pin(6);
         * System.out.
         * println("Output: File 6 pinned in Frame 3; Not already pinned; Evicted file 2 from frame 3\n"
         * );
         */

        Scanner s = new Scanner(System.in);

        if (args.length != 1) {
            System.out.println("Please enter a valid command");
            s.close();
            return;
        } else {
            bp.init(Integer.valueOf(args[0]));
            System.out.println("buffer pool with " + Integer.valueOf(args[0]) +
                    " frames have been initialized.");
        }
        boolean ACTIVE = true;
        while (ACTIVE) {
            System.out.print(">> ");
            String[] command = s.nextLine().split("\\ ", 3);
            switch (command[0]) {
                case ("GET"):
                    bp.get(Integer.valueOf(command[1]));
                    break;
                case ("SET"):
                    bp.set(Integer.valueOf(command[1]), command[2]);
                    break;
                case ("PIN"):
                    bp.pin(Integer.valueOf(command[1]));
                    break;
                case ("UNPIN"):
                    bp.unpin(Integer.valueOf(command[1]));
                    break;
                case ("EXIT"):
                    ACTIVE = false;
                    break;
            }
        }
        s.close();
    }
}
