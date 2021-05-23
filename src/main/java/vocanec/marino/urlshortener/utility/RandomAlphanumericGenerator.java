package vocanec.marino.urlshortener.utility;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Component used for generating random alphanumeric string of desired length.
 * Possible characters in alphanumeric string are: a-z, A-Z, 0-9.
 */
@Component
public class RandomAlphanumericGenerator {

    /**
     * Secure pseudorandom number generator.
     */
    private final SecureRandom random = new SecureRandom();

    /**
     * Method which generates random alphanumeric string of given length.
     * @param length Length of desired random alphanumeric string.
     * @throws IllegalArgumentException If length is not positive integer.
     * @return Random alphanumeric string of given length.
     */
    public String generateAlphanumericString(int length) {
        if(length <= 0) {
            throw new IllegalArgumentException("Length for alphanumeric string must be greater than 0. Was: " + length);
        }

        final String sequence = AlphanumericSequence.getSequence();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            int characterIndex = random.nextInt(sequence.length());
            char character = sequence.charAt(characterIndex);
            sb.append(character);
        }

        return sb.toString();
    }
}
