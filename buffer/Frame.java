package buffer;

public class Frame {
    private byte[] content;
    private boolean dirty;
    private boolean pinned;
    private int blockId;

    /**
     * frame constructor
     */
    public Frame() {
    }

    /**
     * initializing the frame to empty
     */
    public void initialize() {
        this.content = new byte[4096];
        this.dirty = false;
        this.pinned = false;
        this.blockId = -1;
    }

    /**
     * 
     * @return
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * 
     * @param cont
     */
    public void setContent(byte[] cont) {
        this.content = cont;
    }

    /**
     * 
     * @return
     */
    public boolean getDirty() {
        return this.dirty;
    }

    /**
     * 
     * @param dir
     */
    public void setDirty(boolean dir) {
        this.dirty = dir;
    }

    /**
     * 
     * @return
     */
    public boolean getPinned() {
        return this.pinned;
    }

    /**
     * 
     * @param pin
     */
    public void setPinned(boolean pin) {
        this.pinned = pin;
    }

    /**
     * 
     * @return
     */
    public int getBlockId() {
        return this.blockId;
    }

    /**
     * 
     * @param id
     */
    public void setBlockId(int id) {
        this.blockId = id;
    }

    /**
     * 
     * @param recordNum
     * @return
     */
    public String getRecord(int recordNum) {
        byte[] out = new byte[40];
        for (int i = 0; i < 40; i++) {
            int j = (recordNum - 1) * 40 + i;
            out[i] = this.content[j];
        }
        String record = new String(out);
        return record;
    }

    /**
     * 
     * @param recordContent
     * @param recordNum
     */
    public void updateRecord(byte[] recordContent, int recordNum) {
        for (int i = 0; i < 40; i++) {
            int j = (recordNum - 1) * 40 + i;
            this.content[j] = recordContent[i];
        }
        this.dirty = true;
    }
}
