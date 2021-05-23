package vocanec.marino.urlshortener.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configuration for HTTP basic authentication.
 */
@Configuration
@EnableWebSecurity
public class BasicAuthConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Custom authentication provider for HTTP basic authentication which authenticates user.
     */
    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

    /**
     * Configures web security parameters. Cross site request forgery security was disabled for simplicity.
     * Authorization header must be provided for short url registration and retrieval of user statistic. Every
     * other request is publicly available. <code>.headers().frameOptions().disable()</code> is present in order
     * to use <b>h2</b> database configuration panel (console) available at /h2.
     * @param http Object used for configuration.
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/statistic/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authenticationProvider(authenticationProvider);
    }
}