package vocanec.marino.urlshortener.utility;

/**
 * Static class which contains only one <code>String</code> - all possible alphanumeric characters.
 */
public class AlphanumericSequence {

    /**
     * String which contains all possible characters which can occur in generated output.
     * In this case: a-z, A-Z, 0-9.
     */
    private static final String SEQUENCE;

    static {
        StringBuilder sb = new StringBuilder();
        for(char c = 'a'; c <= 'z'; c++) {
            sb.append(c);
        }
        for(char c = 'A'; c <= 'Z'; c++) {
            sb.append(c);
        }
        for(char c = '0'; c <= '9'; c++) {
            sb.append(c);
        }
        SEQUENCE = sb.toString();
    }

    /**
     * Returns alphanumeric sequence (a-z, A-Z, 0-9).
     * @return alphanumeric sequence (all possible alphanumeric characters).
     */
    public static String getSequence() {
        return SEQUENCE;
    }

    /**
     * Private constructor so that no instances of this class can be instantiated.
     * This class is static and its only responsibility is to provide alphanumeric
     * sequence so that code duplication is avoided.
     */
    private AlphanumericSequence() { }
}
