package vocanec.marino.urlshortener.utility;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Component for encoding integer values to base62 which
 * contains all alphanumeric characters (a-z, A-Z, 0-9).
 */
@Component
public class AlphanumericEncoder {

    /**
     * Secure pseudorandom number generator used for generating secure
     * preamble which prevents iterating over all previously generated
     * values.
     */
    private final SecureRandom random = new SecureRandom();

    /**
     * Encodes given integer value to base62and prepends secure preamble.
     * @param value Given value to be encoded.
     * @return Output in base62 with prepended secure preamble.
     */
    public String encode(int value, int preambleLength) {
        if(value <= 0) {
            throw new IllegalArgumentException("Value of a given input must be greater than 0. Was: " + value);
        }

        final String sequence = AlphanumericSequence.getSequence();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < preambleLength; i++) {
            int characterIndex = random.nextInt(sequence.length());
            char character = sequence.charAt(characterIndex);
            sb.append(character);
        }

        while(value > 0) {
            int position = value % sequence.length();
            sb.append(sequence.charAt(position));
            value /= sequence.length();
        }

        return sb.toString();
    }
}
