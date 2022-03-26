package buffer;

public class BufferPool {
    private Frame[] buffers;

    public BufferPool() {

    }

    /**
     * 
     * @param bufferNum
     */
    public void initialize(int bufferNum) {
        this.buffers = new Frame[bufferNum];
        for (int i = 0; i < bufferNum; i++) {
            buffers[i] = new Frame();
            buffers[i].initialize();
        }
    }

    /**
     * 
     * @param blockId
     * @return
     */
    public int bufferNum(int blockId) {

        return -1;
    }

    /**
     * 
     * @param blockId
     * @return
     */
    public byte[] blockContent(int blockId) {
        int bufferNumber = bufferNum(blockId);
        return buffers[bufferNumber].getContent();
    }

    /**
     * 
     * @param blockId
     */
    public void readBlock(int blockId) {

    }

    /**
     * 
     * @return
     */
    public int emptyFrame() {
        return 0;
    }

    /**
     * 
     */
    public void overwriteFrame() {

    }

}
