import java.util.Hashtable;

public class HashIndex<Key, String> {
    private Hashtable<Integer,String> ht;

    public HashIndex (){
        this.ht = new Hashtable<Integer,String>();
    }

    public void put(int file, int offset, int randomV) {
        String str = ht.get(randomV);
        String newS = str.concat(file);
    }

    public void get(int randomV) {

    }



}
