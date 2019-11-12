import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Scanner;

public class TestHelper {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static SecureRandom random = new SecureRandom();

    public String generateRandomWord() {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < sb.capacity(); i++) {
            int rndCharAt = random.nextInt(CHARS.length());
            char rndChar = CHARS.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }

    public Boolean isFileContainsWord(File file, String word) throws IOException {

        /*final Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            final String lineFromFile = scanner.nextLine();
            if (lineFromFile.) {
                System.out.println("Match! Found word: " + word);
                return true;
            }
        }
        System.out.println("Word not found");
        return false;*/

        boolean result = false;

        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.contains(word) && !result) {
                    result = true;
                    System.out.println("ho hum, i found it on line " +lineNum);
                }
            }
            return result;
        } catch(FileNotFoundException e) {
            throw new RuntimeException (e);
        }

    }

}
