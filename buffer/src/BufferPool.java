import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

public class BufferPool {
    private Frame[] buffers;
    private BitSet map;
    private int lastEvited;
    private int oldID;

    public BufferPool() {

    }

    /**
     * initialize the buffer pool with input number of slots
     * 
     * @param bufferNum
     */
    public void init(int bufferNum) {
        this.buffers = new Frame[bufferNum];
        for (int i = 0; i < bufferNum; i++) {
            buffers[i] = new Frame();
            buffers[i].initialize();
        }
        this.map = new BitSet(bufferNum);
        this.lastEvited = 0;
        this.oldID = 0;
    }

    /**
     * 
     * @param record
     * @param blockId
     * @throws IOException
     */
    public void set(int recordNum, String newRecord) throws IOException {
        String recordStr = newRecord.substring(0, 40);
        int blockId;
        if (recordNum % 100 == 0)
            blockId = recordNum / 100;
        else
            blockId = recordNum / 100 + 1;
        recordNum = recordNum % 100;
        byte[] record = recordStr.getBytes();
        // case 1: in memory
        int slot = bufferNum(blockId);
        if (slot != -1) {
            buffers[slot].updateRecord(record, recordNum);
            System.out.println(
                    "Output: Write was successful; File " + blockId + " already in memory; Located in Frame "
                            + (slot + 1));
            return;
        } else {
            // case 2: not in memory but has empty buffer slots
            if (this.map.cardinality() != this.buffers.length) {
                slot = readBlock(blockId);
                buffers[slot].updateRecord(record, recordNum);
                System.out.println(
                        "Output: Write was successful; Brought File " + blockId + " from disk; Placed in Frame "
                                + (slot + 1));
                return;
            }

            else {
                slot = readBlock(blockId);
                lastEvited++;
                // case 4: no block can be taken out
                if (slot == -1) {
                    System.out.println("Output: Write was unsuccessful; The corresponding block " + blockId
                            + " cannot be accessed from disk because the memory buffers are full");
                    return;
                }

                // case 3: no empty buffer slots but some block can be taken out
                buffers[slot].updateRecord(record, recordNum);
                System.out.println("Output: Write was successful; Brought File " + blockId +
                        " from disk; Placed in Frame " + (slot + 1) + "; Evicted File " + oldID + " from frame "
                        + (slot + 1));
                return;
            }
        }
    }

    /**
     * get record function
     * has 4 cases
     * 
     * @param recordNum
     * @throws IOException
     */
    public void get(int recordNum) throws IOException {
        int blockId;
        if (recordNum % 100 == 0)
            blockId = recordNum / 100;
        else
            blockId = recordNum / 100 + 1;

        // case 1: in memory
        int slot = bufferNum(blockId);
        if (slot != -1) {
            System.out.println("Output: " + buffers[slot].getRecord(recordNum % 100) + "; File " + blockId
                    + " already in memory; Located in Frame " + (slot + 1));
            return;
        }

        else {
            // case 2: not in memory but has empty buffer slots
            if (this.map.cardinality() != this.buffers.length) {
                slot = readBlock(blockId);
                System.out.println("Output: " + buffers[slot].getRecord(recordNum % 100) + "; Brought File " + blockId
                        + " from disk; Placed in Frame " + (slot + 1));
                return;
            }

            else {
                slot = readBlock(blockId);
                lastEvited++;
                if (slot == -1) {
                    // case 4: there is no available frame, nor can any be evicted
                    System.out.println("Output: The corresponding block " + blockId
                            + " cannot be accessed from disk because the memory buffers are full");
                    return;
                }

                // case 3: no empty buffer slots but some block can be taken out (already
                // handled in readBlock function)
                System.out.println("Output: " + buffers[slot].getRecord(recordNum % 100) + "; Brought File " + blockId +
                        " from disk; Placed in Frame " + (slot + 1) + "; Evicted File " + oldID
                        + " from frame " + (slot + 1));
                return;
            }
        }
    }

    /**
     * pin a file/block in a frame
     * 
     * @param file
     * @throws IOException
     */
    public void pin(int file) throws IOException {
        for (int i = 0; i < buffers.length; i++) {
            // case 1: file already in memory
            if (map.get(i) && buffers[i].getBlockId() == file) {
                if (buffers[i].getPinned()) {
                    System.out.println("Output: File " + file + " pinned in Frame " + (i + 1) + "; Already pinned");
                } else {
                    buffers[i].setPinned(true);
                    System.out.println("Output: File " + file + " pinned in Frame " + (i + 1) + "; Not already pinned");
                }
                return;
            }
        }
        int slot = -1;
        // case 2: file block not in memory, but can be brought into pool; there are
        // empty frames
        if (this.map.cardinality() != this.buffers.length) {
            slot = readBlock(file);
            if (buffers[slot].getPinned()) {
                System.out.println("Output: File " + file + " pinned in Frame " + (slot + 1) + "; Already pinned");
            } else {
                buffers[slot].setPinned(true);
                System.out.println("Output: File " + file + " pinned in Frame " + (slot + 1) + "; Not already pinned");
            }
            return;
        } else {
            slot = readBlock(file);
            lastEvited++;
            if (slot == -1) {
                // case 4: no buffer pool available
                System.out.println(
                        "Output: The corresponding block " + file
                                + " cannot be pinned because the memory buffers are full");
                return;
            }

            // case 3: there are no empty frames, but can evict an unpinned frame (action
            // already done in readBlock(file) function)
            if (buffers[slot].getPinned()) {
                System.out.println("Output: File " + file + " pinned in Frame " + (slot + 1) +
                        "; Already pinned; Evicted File " + oldID + " from frame " + (slot + 1));
            } else {
                buffers[slot].setPinned(true);
                System.out.println("Output: File " + file + " pinned in Frame " + (slot + 1) +
                        "; Not already pinned; Evicted File " + oldID + " from frame " + (slot + 1));
            }
            return;
        }
    }

    /**
     * unpin a frame with given file number
     * 
     * @param file number
     */
    public void unpin(int file) {
        // we have a loop and check if the file number is equal to the frame ID
        for (int i = 0; i < buffers.length; i++) {
            if (map.get(i) && buffers[i].getBlockId() == file) {
                // case 1: file already in memory
                if (buffers[i].getPinned()) {
                    buffers[i].setPinned(false);
                    System.out.println(
                            "Output: File " + file + " unpinned in Frame " + (i + 1) + "; Was not already unpinned");
                } else {
                    System.out.println("Output: File " + file + " unpinned in Frame " + (i + 1) + "; Already unpinned");
                }
                return;
            }
        }
        // case 2: file not in memory
        System.out.println(
                "Output: The corresponding block " + file + " cannot be unpinned because it is not in memory.");
    }

    /**
     * We write the block content back to file
     * 
     * @param file
     */
    public void fileIO(int file, byte[] data) {
        String FILEPATH = "";
        switch (file) {
            case 1:
                FILEPATH = "Project1/F1.txt";
                break;
            case 2:
                FILEPATH = "Project1/F2.txt";
                break;
            case 3:
                FILEPATH = "Project1/F3.txt";
                break;
            case 4:
                FILEPATH = "Project1/F4.txt";
                break;
            case 5:
                FILEPATH = "Project1/F5.txt";
                break;
            case 6:
                FILEPATH = "Project1/F6.txt";
                break;
            case 7:
                FILEPATH = "Project1/F7.txt";
                break;
        }

        File f = new File(FILEPATH);
        try {
            OutputStream os = new FileOutputStream(f);
            os.write(data);
            os.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    /**
     * find the frame number that holds block id
     * 
     * @param blockId
     * @return the slot number if block in buffer, -1 if not
     */
    public int bufferNum(int blockId) {
        for (int i = 0; i < this.buffers.length; i++) {
            if (map.get(i) && buffers[i].getBlockId() == blockId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * return block content when block is in buffer
     * 
     * @param blockId
     * @return the content of the block
     */
    public byte[] blockContent(int blockId) {
        int bufferNumber = bufferNum(blockId);
        return buffers[bufferNumber].getContent();
    }

    /**
     * when the block is not in buffer, we need to read block from disk
     * 
     * @param blockId
     * @throws IOException
     */
    public int readBlock(int blockId) throws IOException {
        Path path;
        byte[] data = new byte[4096];
        path = Paths.get("Project1/F" + blockId + ".txt");
        data = Files.readAllBytes(path);

        // if there is an empty frame
        if (map.cardinality() != buffers.length) {
            for (int i = 0; i < buffers.length; i++) {
                if (!map.get(i)) {
                    buffers[i].setBlockId(blockId);
                    buffers[i].setContent(data);
                    this.map.set(i);
                    return i;
                }
            }
            return 0;
            // when there is no empty frame
        } else {
            // keep a record of the old value
            int a = lastEvited;
            if (lastEvited == buffers.length)
                lastEvited = 0;
            int i = lastEvited;
            while (buffers[lastEvited].getPinned()) {
                lastEvited++;
                if (lastEvited == buffers.length)
                    lastEvited = 0;
                // this is when there are not unpinned block and we all the way around our index
                if (lastEvited == i) {
                    // we want to back to old value, since lastEvicted increments afterwards in the
                    // upper level functions, keep it as a-1
                    lastEvited = a - 1;
                    return -1;
                }
            }
            this.oldID = buffers[lastEvited].getBlockId();

            // if the frame is dirty and need to write to IO
            if (buffers[lastEvited].getDirty()) {
                fileIO(oldID, buffers[lastEvited].getContent());
            }
            // we overwrite the frame content with given block id
            buffers[lastEvited].setBlockId(blockId);
            buffers[lastEvited].setContent(data);
            this.map.set(lastEvited);
            return lastEvited;
        }
    }
}
