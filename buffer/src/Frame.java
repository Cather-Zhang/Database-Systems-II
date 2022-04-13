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
     * getter
     * 
     * @return content
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * setter
     * 
     * @param cont
     */
    public void setContent(byte[] cont) {
        this.content = cont;
    }

    /**
     * getter
     * 
     * @return dirty
     */
    public boolean getDirty() {
        return this.dirty;
    }

    /**
     * setter
     * 
     * @param dir
     */
    public void setDirty(boolean dir) {
        this.dirty = dir;
    }

    /**
     * getter
     * 
     * @return
     */
    public boolean getPinned() {
        return this.pinned;
    }

    /**
     * setter
     * 
     * @param pin
     */
    public void setPinned(boolean pin) {
        this.pinned = pin;
    }

    /**
     * getter
     * 
     * @return blockId
     */
    public int getBlockId() {
        return this.blockId;
    }

    /**
     * setter
     * 
     * @param id
     */
    public void setBlockId(int id) {
        this.blockId = id;
    }

    /**
     * get the record (as a string) with record number
     * 
     * @param recordNum
     * @return record
     */
    public String getRecord(int recordNum) {
        if (recordNum == 0)
            recordNum = 100;
        byte[] out = new byte[40];
        for (int i = 0; i < 40; i++) {
            int j = (recordNum - 1) * 40 + i;
            out[i] = this.content[j];
        }
        String record = new String(out);
        return record;
    }

    /**
     * update the record given record number and new content
     * 
     * @param recordContent
     * @param recordNum
     */
    public void updateRecord(byte[] recordContent, int recordNum) {
        if (recordNum == 0)
            recordNum = 100;
        for (int i = 0; i < 40; i++) {
            int j = (recordNum - 1) * 40 + i;
            this.content[j] = recordContent[i];
        }
        this.dirty = true;
    }
}
