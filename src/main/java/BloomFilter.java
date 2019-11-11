import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

public class BloomFilter {

    private BitSet bf = new BitSet();
    private int bitSetSize = 100;

    public void add(byte[] bytes) {
        int [] hashes = getHashes(bytes, 4);
        for (int hash : hashes) {
            bf.set(Math.abs(hash % bitSetSize), true); // modulo is used to get smaller integer for bitset index
        }
    }

    public boolean check(byte[] bytes) {
        int [] hashes = getHashes(bytes, 4);
        for (int hash : hashes) {
            if (!bf.get(Math.abs(hash % bitSetSize))) {
                return false;
            }
        }
        return true;
    }

    public byte[] getMessageDigest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Split MD5 digest to hashes
    public int[] getHashes(byte[] bytes, int chunksize) {
        int[] hashes = new int[4];
        int i = 0; // array of hashes index
        int start = 0;
        System.out.println("Source length : " + bytes.length);
        while (start < bytes.length) {
            int end = Math.min(bytes.length, start + chunksize);
            System.out.println("Start: " + start);
            System.out.println("End: " + end);
            hashes[i] = (getIntFromBitArray(Arrays.copyOfRange(bytes, start, end), 0));
            System.out.println("Hash " + i + " " + hashes[i]);
            start += chunksize;
            i++;
        }
        return hashes;
    }

    // byte array to int
    public int getIntFromBitArray(byte[] bytes, int offset){
        System.out.println("Raw length: " + bytes.length);
        System.out.println("Offset: " + offset);
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, 4);

        return buffer.getInt();
    }

    public BitSet getBloomFilter() {
        return this.bf;
    }


}
