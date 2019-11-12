import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

public class BloomFilter {

    private BitSet bf;
    private int filterSize; // m
    private int numberOfElements; // n
    private int numberOfHashes = 4; // k since we are using 128-bit MD5 and want to use 32-bit int, numberOfHashes could be either 2 or 4

    public BloomFilter(int numberOfElements) {
        filterSize = numberOfElements * numberOfHashes;
        this.numberOfHashes = numberOfHashes;
        this.bf = new BitSet(filterSize);
    }

    public void add(byte[] bytes) {
        int [] hashes = getHashes(bytes, numberOfHashes);
        for (int hash : hashes) {
            bf.set(Math.abs(hash % filterSize), true); // modulo is used to get smaller integer for bitset index
        }
    }

    public boolean check(byte[] bytes) {
        int [] hashes = getHashes(bytes, numberOfHashes);
        for (int hash : hashes) {
            if (!bf.get(Math.abs(hash % filterSize))) {
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
    public int[] getHashes(byte[] bytes, int numberOfHashes) {
        int[] hashes = new int[numberOfHashes];
        int i = 0; // array of hashes index
        int start = 0;
        while (start < bytes.length) {
            int end = Math.min(bytes.length, start + bytes.length/numberOfHashes);
            hashes[i] = (getIntFromBitArray(Arrays.copyOfRange(bytes, start, end)));
            start += bytes.length/numberOfHashes;
            i++;
        }
        return hashes;
    }

    // byte array to int
    public int getIntFromBitArray(byte[] bytes){
        return ByteBuffer.wrap(bytes).getInt();
    }

    public BitSet getBloomFilter() {
        return this.bf;
    }

    public int getFilterSize() {
        return bf.size();
    }

    public int getFilterLength() {
        return bf.length();
    }

    public void clearBloomFilter() {
        bf.clear();
    }


}
