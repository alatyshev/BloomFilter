import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BloomFilterTest {

    private TestHelper testHelper = new TestHelper();

    @ValueSource (ints = {2,4})
    @ParameterizedTest
    public void testGetHashes(int numberOfHashes) {
        BloomFilter filter = new BloomFilter(10);
        byte[] md = filter.getMessageDigest("test");
        int[] resultHashes = filter.getHashes(md, numberOfHashes);
        assertEquals(numberOfHashes, resultHashes.length);
    }

    @Test
    void singleItemAddAndCheck() {
        BloomFilter filter = new BloomFilter(10);
        filter.add(filter.getMessageDigest("Dude"));
        assertTrue(filter.check(filter.getMessageDigest("Dude")),
                "Dude should be in the set");
        assertFalse(filter.check(filter.getMessageDigest("Dudetta")),
                "Dudetta shouldn't be in the set");
    }

    @Test
    void smallWordListAddAndCheck() throws IOException {

        BloomFilter filter = new BloomFilter(100);
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/wordlist_small.csv"))) {
            String word;
            while ((word = br.readLine()) != null) {
                byte[] md = filter.getMessageDigest(word);
                System.out.println("Adding word to filter: " + word);
                filter.add(md);
                assertTrue(filter.check(md));
            }
        }
        String randomWord = testHelper.generateRandomWord();
        assertFalse(filter.check(filter.getMessageDigest(randomWord))
                , "False positive! Random word " + randomWord + " shouldn't be in the set");
    }

    @Test
    void largeWordListAddAndCheck() throws IOException {
        String largeFilePath = "src/test/resources/wordlist_large.csv";
        BloomFilter filter = new BloomFilter(200000000);
        try (BufferedReader br = new BufferedReader(new FileReader(largeFilePath))) {
            String word;
            while ((word = br.readLine()) != null) {
                byte[] md = filter.getMessageDigest(word);
                filter.add(md);
            }
        }
        int falsePositivesCount = 0;
        for(int i = 0; i < 10000; i++){
            String randomWord = testHelper.generateRandomWord();
            if (filter.check(filter.getMessageDigest(randomWord))) {
                System.out.println("Random word matched in filter: " + randomWord);
                if ( ! testHelper.isFileContainsWord(new File(largeFilePath), randomWord) ) {
                    falsePositivesCount ++;
                    System.out.println("False positive word: " + randomWord);
                }
            }
        }
        System.out.println("False positives count: " + falsePositivesCount);

    }


}
