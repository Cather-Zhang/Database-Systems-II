package buffer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferPool bp = new BufferPool();
        bp.init(3);
        bp.set(430, "F05-Rec450, Jane Do, 10 Hill Rd, age020.");
        bp.get(430);
        bp.get(20);
        bp.set(430, "F05-Rec450, John Do, 23 Lake Ln, age056.");
        bp.pin(5);
        bp.unpin(3);
        bp.get(430);
        bp.pin(5);
        bp.get(646);
        bp.pin(3);
        bp.set(10, "F01-Rec010, Tim Boe, 09 Deer Dr, age009.");
        bp.unpin(1);
        bp.get(355);
        bp.pin(2);
        bp.get(156);
        bp.set(10, "F01-Rec010, No Work, 31 Hill St, age100.");
        bp.pin(7);
        bp.get(10);
        bp.unpin(3);
        bp.unpin(2);
        bp.get(10);
        bp.pin(6);
    }
}
