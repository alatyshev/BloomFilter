import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BloomFilterTest {

    private BloomFilter filter = new BloomFilter();

    @BeforeAll
    private static void init() {

    }

    @Test
    public void singleItemAddAndCheck() {
        filter.add(filter.getMessageDigest("Dude"));
        assertTrue(filter.check(filter.getMessageDigest("Dude")),
                "Dude should be in the set");
        assertFalse(filter.check(filter.getMessageDigest("Dudetta")),
                "Dudetta shouldn't be in the set");
    }

}
