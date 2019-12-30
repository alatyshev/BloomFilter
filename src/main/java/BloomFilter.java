import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

class BloomFilter {

    private final BitSet bf;
    private final int filterSize; // m
    private final int numberOfElements; // n
    private final int numberOfHashes; // k

    BloomFilter(int numberOfElements, int numberOfHashes, int filterSize) {
        this.numberOfElements = numberOfElements;
        this.numberOfHashes = numberOfHashes;
        this.filterSize = filterSize;
        bf = new BitSet(filterSize);
    }

    void add(byte[] bytes) {
        int [] hashes = getHashes(bytes, numberOfHashes);
        for (int hash : hashes) {
            bf.set(Math.abs(hash % filterSize), true); // modulo is used to get smaller integer for bitset index
        }
    }

    boolean check(byte[] bytes) {
        int [] hashes = getHashes(bytes, numberOfHashes);
        for (int hash : hashes) {
            if (!bf.get(Math.abs(hash % filterSize))) {
                return false;
            }
        }
        return true;
    }

    static byte[] getMessageDigest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Split MD5 digest to hashes
    int[] getHashes(byte[] bytes, int numberOfHashes) {
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
    private int getIntFromBitArray(byte[] bytes){
        return ByteBuffer.wrap(bytes).getInt();
    }

    /* False Positive probability is calculated as:
     P = (1 - e^(-k * n / m)) ^ k
     where:
     k is number of hashes
     m is filter size
     n is expected number of elements in the set
     */
    double calculateFalsePositiveProbability() {
        return Math.pow((1 - Math.exp(-numberOfHashes * (double) numberOfElements
                / (double) filterSize)), numberOfHashes);
    }


}
