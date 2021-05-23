package vocanec.marino.urlshortener.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component whose purpose is to encode passwords before they are stored in the database.
 */
@Component
public class PasswordEncoder {

    /**
     * Password encoder (Adapter pattern).
     */
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Encodes given password.
     * @param password Password to encode.
     * @return Encoded password.
     */
    public String encode(String password) {
        return encoder.encode(password);
    }

    /**
     * Checks whether raw password matches encoded (stored) password.
     * @param rawPassword Password to check.
     * @param encodedPassword Encoded (stored) password.
     * @return True if raw password matches encoded password, false otherwise.
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
