import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Scanner;

class TestHelper {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();

    static String generateRandomWord() {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < sb.capacity(); i++) {
            int rndCharAt = random.nextInt(CHARS.length());
            char rndChar = CHARS.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }

    static Boolean isFileContainsWord(File file, String word) throws IOException {

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String lineFromFile = scanner.nextLine();
                if (lineFromFile.contains(word)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
            return false;
        } catch(FileNotFoundException e) {
            throw new RuntimeException (e);
        }

    }

}
