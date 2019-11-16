import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BloomFilterTest {

    @Test
    void singleItemAddAndCheck() {
        BloomFilter bf = new BloomFilter(1, 2, 100);
        bf.add(BloomFilter.getMessageDigest("Dude"));
        assertTrue(bf.check(BloomFilter.getMessageDigest("Dude")), "Dude should be in the set");
        assertFalse(bf.check(BloomFilter.getMessageDigest("Dudetta")), "Dudetta shouldn't be in the set");
    }

    @Test
    void smallWordListAddAndCheck() throws IOException {

        BloomFilter bf = new BloomFilter(100, 4, 10000);
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/wordlist_small.txt"))) {
            String word;
            while ((word = br.readLine()) != null) {
                byte[] md = BloomFilter.getMessageDigest(word);
                bf.add(md);
                assertTrue(bf.check(md));
            }
        }
        assertFalse(bf.check(BloomFilter.getMessageDigest("wolf"))
                , "False positive! Word 'wolf' shouldn't be in the set");
        assertFalse(bf.check(BloomFilter.getMessageDigest("bear"))
                , "False positive! Word 'dog' shouldn't be in the set");
    }

    @ParameterizedTest
    @CsvFileSource(resources= "/bloom-filter-test-parameters.csv", numLinesToSkip = 1)
    @DisplayName("Reads large word list, adds to filter and checks random 5 character strings in the filter, " +
            "if false positive found checks their if the string is in the file, " +
            "compares actual vs. expected probability of false positives")
    void largeWordListAddAndCheckAgainstRandomStrings(int numberOfHashes, int filterSize, int randomStringProbesCount) throws IOException {
        int numberOfElements = 338882; //number of lines in large wordlist
        String largeFilePath = "src/test/resources/wordlist_large.txt";
        BloomFilter bf = new BloomFilter(numberOfElements, numberOfHashes, filterSize);

        // add words from file to filter
        try (BufferedReader br = new BufferedReader(new FileReader(largeFilePath))) {
            String word;
            while ((word = br.readLine()) != null) {
                byte[] md = BloomFilter.getMessageDigest(word);
                bf.add(md);
            }
        }

        double falsePositiveProbability = bf.calculateFalsePositiveProbability(numberOfElements, numberOfHashes);
        int falsePositivesCount = 0;

        /* check if random 5 chars string is in filter,
         if found in filter check from file, if not found in file count as false positive */
        for(int i = 0; i < randomStringProbesCount; i++){
            String randomWord = TestHelper.generateRandomWord();
            if (bf.check(BloomFilter.getMessageDigest(randomWord))) {
                if (!TestHelper.isFileContainsWord(new File(largeFilePath), randomWord)) {
                    falsePositivesCount ++;
                }
            }
        }
        double actualFalsePositiveRate = (double) falsePositivesCount / (double) randomStringProbesCount;
        System.out.println();
        System.out.println("Test run with: " + "numberOfHashes=" + numberOfHashes + ", filterSize= " +filterSize);
        System.out.println("===================================================");
        System.out.println("False Positives Expected Probability: " + falsePositiveProbability);
        System.out.println("False positives count: " + falsePositivesCount + " out of " + randomStringProbesCount + " probes");
        System.out.println("False Positives Actual Rate: " + actualFalsePositiveRate);

        /* Actual false positives rate is stochastic, so asserting it against expected probability may fail sometimes,
        still it should be close enough to expected one. Should be analysed statistically */

        //assertTrue(actualFalsePositiveRate <= falsePositiveProbability);
    }

    @ValueSource (ints = {2,4})
    @ParameterizedTest
    public void testGetHashes(int numberOfHashes) {
        BloomFilter bf = new BloomFilter(10, numberOfHashes, 1000);
        byte[] md = BloomFilter.getMessageDigest("test");
        int[] resultHashes = bf.getHashes(md, numberOfHashes);
        assertEquals(numberOfHashes, resultHashes.length);
    }

}