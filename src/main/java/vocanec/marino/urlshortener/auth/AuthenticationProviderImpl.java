package vocanec.marino.urlshortener.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import vocanec.marino.urlshortener.services.AccountService;

import java.util.Collections;

/**
 * Custom authentication provider for HTTP basic authentication.
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    /**
     * Account service which allows user authentication.
     */
    @Autowired
    private AccountService accountService;

    /**
     * Authenticates user with provided HTTP basic authentication information.
     * @param authentication Authentication object which contains information about provided username and password.
     * @return <code>UsernamePasswordAuthenticationToken</code> upon successful authentication.
     * @throws AuthenticationException
     * @throws BadCredentialsException If credentials are not correct.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if(!accountService.authenticate(username, password)) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                username,
                password,
                Collections.emptyList()
        );
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
