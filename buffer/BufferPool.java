package buffer;

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

    public BufferPool() {

    }

    /**
     * initialize the buffer pool with input number of slots
     * @param bufferNum
     */
    public void init(int bufferNum) {
        this.buffers = new Frame[bufferNum];
        for (int i = 0; i < bufferNum; i++) {
            buffers[i] = new Frame();
            buffers[i].initialize();
        }
        this.map = new BitSet(bufferNum);
    }

    /**
     * 
     * @param record
     * @param blockId
     * @throws IOException
     */
    public void set(int recordNum, String recordStr) throws IOException {
        int blockId = recordNum / 100 + 1;
        recordNum = recordNum % 100;
        byte[] record = recordStr.getBytes();
        //case 1: in memory
        int slot = bufferNum(blockId);
        if (slot != -1) {
            buffers[slot].updateRecord(record, recordNum);
            System.out.println("Write was successful; File " + blockId + " already in memory; Located in Frame " + (slot + 1));
            return;
        }
        else {
            //case 2: not in memory but has empty buffer slots
            if (this.map.cardinality() != this.buffers.length) {
                slot = readBlock(blockId);
                buffers[slot].updateRecord(record, recordNum);
                System.out.println("Write was successful; Brought File " + blockId + " from disk; Placed in Frame " + (slot + 1));
                return;
            }

            else {
                //case 3: no empty buffer slots but some block can be taken out
                for (int i = 0; i < buffers.length; i++) {
                    if (!buffers[i].getPinned()) {
                        int oldID = buffers[i].getBlockId();
       
                        if (buffers[i].getDirty()) {
                            //case 3(2): block is not written
                            fileIO(oldID, buffers[i].getContent());
                        }
                        slot = readBlock(blockId);
                        buffers[slot].updateRecord(record, recordNum);
                        System.out.println("Write was successful; Brought File " +blockId +
                            " from disk; Placed in Frame " + (slot+1) + "; Evicted File "+ oldID +" from frame " + (slot + 1));
                        return;                   
                    }                   
                }
                //case 4: no block can be taken out
                System.out.println("The corresponding block "+ blockId + " cannot be accessed from disk because the memory buffers are full");
            }
        }
    }

    /**
     * get record function
     * has 4 cases
     * @param recordNum
     * @throws IOException
     */
    public void get(int recordNum) throws IOException{
        int blockId = recordNum / 100 + 1;

        //case 1: in memory
        int slot = bufferNum(blockId);
        if (slot != -1) {
            System.out.println(buffers[slot].getRecord(recordNum % 100) + "; File " + blockId + " already in memory; Located in Frame " + (slot + 1));
            return;
        }

        
        else {
            //case 2: not in memory but has empty buffer slots
            if (this.map.cardinality() != this.buffers.length) {
                slot = readBlock(blockId);
                System.out.println(buffers[slot].getRecord(recordNum % 100) + "; Brought File " + blockId + " from disk; Placed in Frame " + (slot + 1));
                return;
            }

            else {
                //case 3: no empty buffer slots but some block can be taken out
                for (int i = 0; i < buffers.length; i++) {
                    if (!buffers[i].getPinned()) {
                        int oldID = buffers[i].getBlockId();
                                  
                        if (!buffers[i].getDirty()) {
                            //case 3(1): block is not written
                            slot = readBlock(blockId);
                            System.out.println(buffers[slot].getRecord(recordNum % 100) + "; Brought File " +blockId +
                             " from disk; Placed in Frame " + (slot + 1) + "; Evicted File "+ oldID +" from frame " + (slot + 1));
                            return;
                        }
                        else {
                            //case 3(2): need to update to disk
                            fileIO(oldID, buffers[i].getContent());
                            slot = readBlock(blockId);
                            System.out.println(buffers[slot].getRecord(recordNum % 100) + "; Brought File " +blockId +
                             " from disk; Placed in Frame " + (slot + 1) + "; Evicted File "+ oldID +" from frame " + (slot + 1));
                            return;
                        }  
                    }
                }

                //case 4: no block can be taken out
                System.out.println("The corresponding block "+ blockId + " cannot be accessed from disk because the memory buffers are full");
            }
        }
    }


    /**
     * 
     * @param file
     * @throws IOException
     */
    public void pin(int file) throws IOException {
        for (int i = 0; i < buffers.length; i++) {
            //case 1: file already in memory
            if (map.get(i) && buffers[i].getBlockId() == file) {
                if (buffers[i].getPinned())
                {
                    System.out.println("File " + file + " pinned in Frame " + (i+1) + "; Already pinned");
                }
                else {
                    buffers[i].setPinned(true);
                    System.out.println("File " + file + " pinned in Frame " + (i+1) + "; Not already pinned");
                }
                return;
            }
        }
        int slot = -1;
        //case 2: file block not in memory, but can be brought into pool
        if (this.map.cardinality() != this.buffers.length) {
            slot = readBlock(file);
            if (buffers[slot].getPinned())
            {
                System.out.println("File " + file + " pinned in Frame " + (slot+1) + "; Already pinned");
            }
            else {
                buffers[slot].setPinned(true);
                System.out.println("File " + file + " pinned in Frame " + (slot+1) + "; Not already pinned");
            }
            return;
        }
        else {
            for (int i = 0; i < buffers.length; i++) {
                if (!buffers[i].getPinned()) {
                    int oldID = buffers[i].getBlockId();
                              
                    if (!buffers[i].getDirty()) {
                        //case 3(1): block is not written
                        slot = readBlock(file);
                        if (buffers[slot].getPinned())
                        {
                            System.out.println("File " + file + " pinned in Frame " + (slot+1) + 
                            "; Already pinned; Evicted File "+ oldID +" from frame " + (slot + 1));
                        }
                        else {
                            buffers[slot].setPinned(true);
                            System.out.println("File " + file + " pinned in Frame " + (slot+1) +
                             "; Not already pinned; Evicted File "+ oldID +" from frame " + (slot + 1));
                        }               
                        return;
                    }

                    else {
                        //case 3(2): need to update to disk
                        fileIO(oldID, buffers[i].getContent());
                        slot = readBlock(file);
                        if (buffers[slot].getPinned())
                        {
                            System.out.println("File " + file + " pinned in Frame " + (slot+1) + 
                            "; Already pinned; Evicted File "+ oldID +" from frame " + (slot + 1));
                        }
                        else {
                            buffers[slot].setPinned(true);
                            System.out.println("File " + file + " pinned in Frame " + (slot+1) +
                             "; Not already pinned; Evicted File "+ oldID +" from frame " + (slot + 1));
                        }               
                        return;
                    }  
                }
            }
            //case 3: no buffer pool available
            System.out.println("The corresponding block "+ file +" cannot be pinned because the memory buffers are full");
        }
    }

    public void unpin(int file) {
        for (int i = 0; i < buffers.length; i++) {
            //case 1: file already in memory
            if (map.get(i) && buffers[i].getBlockId() == file) {
                if (buffers[i].getPinned())
                {
                    buffers[i].setPinned(false);
                    System.out.println("File " + file + " unpinned in Frame " + (i+1) + "; Was not already unpinned");
                }
                else {
                    System.out.println("File " + file + " unpinned in Frame " + (i+1) + "; Already unpinned");
                }
                return;
            }
        }

        //case 2: file not in memory
        System.out.println("The corresponding block "+ file + " cannot be unpinned because it is not in memory.");
    }

    /**
     * 
     * @param file
     */
    public void fileIO(int file, byte[] data) {
        String FILEPATH = "";
        switch (file) {
            case 1: 
                FILEPATH = "buffer/Project1/F1.txt";
                break;
            case 2: 
                FILEPATH = "buffer/Project1/F2.txt";
                break;
            case 3: 
                FILEPATH = "buffer/Project1/F3.txt";
                break;
            case 4: 
                FILEPATH = "buffer/Project1/F4.txt";
                break;
            case 5: 
                FILEPATH = "buffer/Project1/F5.txt";
                break;
            case 6: 
                FILEPATH = "buffer/Project1/F6.txt";
                break;
            case 7: 
                FILEPATH = "buffer/Project1/F7.txt";
                break;
        }
        
        File f = new File(FILEPATH);
        try {
            OutputStream os = new FileOutputStream(f);
            os.write(data);
            os.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }



    /**
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
     * @param blockId
     * @return the content of the block
     */
    public byte[] blockContent(int blockId) {
        int bufferNumber = bufferNum(blockId);
        return buffers[bufferNumber].getContent();
    }

    /**
     * when the block is not in buffer, we need to read block from disk
     * @param blockId
     * @throws IOException
     */
    public int readBlock(int blockId) throws IOException {
        Path path;
        byte[] data = new byte[4096];
        path = Paths.get("buffer/Project1/F"+ blockId +".txt");
        data = Files.readAllBytes(path);
        
        //if there is an empty frame
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
        }
        else {
            for (int i = 0; i < buffers.length; i++) {
                if (!buffers[i].getPinned()) {
                    buffers[i].setBlockId(blockId);
                    buffers[i].setContent(data);
                    this.map.set(i);
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * 
     * @return 
     */
    public Frame emptyFrame() {
        for (int i = 0; i < buffers.length; i++) {
            if (!map.get(i)) 
            return buffers[i];
        }
        return null;
    }

    /**
     * 
     */
    public void eviction() {

    }

}
