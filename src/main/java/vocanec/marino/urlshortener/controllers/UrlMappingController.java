package vocanec.marino.urlshortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vocanec.marino.urlshortener.models.UrlMapping;
import vocanec.marino.urlshortener.responses.UrlMappingResponse;
import vocanec.marino.urlshortener.services.UrlMappingService;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.LOCATION;

/**
 * Controller which contains mappings for new URL creation (registration),
 * statistic retrieval and redirection of short urls to long urls.
 */
@RestController
public class UrlMappingController {

    /**
     * Url mapping service which contains business logic for url mappings.
     */
    @Autowired
    private UrlMappingService urlMappingService;

    /**
     * Mapping for short url creation (registration). Authorization header must be provided with correct
     * user credentials in order to successfully access this route.
     * @param urlMapping Url mapping which should contain one mandatory property: <code>url</code> to be shorten, and
     *                   optional <code>redirectType</code> (301 or 302)
     * @return <code>UrlMappingResponse</code> object which encapsulates all information about success of the call.
     */
    @PostMapping("/register")
    public ResponseEntity<UrlMappingResponse> createUrlMapping(@RequestBody UrlMapping urlMapping) {
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        UrlMappingResponse response = urlMappingService.createUrlMapping(accountId, urlMapping);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    /**
     * Mapping for retrieval of statistic map for the user with <code>accountId</code>. Authorization header
     * with correct credentials must be provided. What is more, each user can fetch only it's own statistic data.
     * It is forbidden to access other user statistic data - returns 403 Forbidden.
     * @param accountId Path variable which represents account identifier (only alphanumeric characters).
     * @return Map whose key are stored user's long urls and whose values are corresponding visit counts.
     */
    @GetMapping("/statistic/{accountId:[a-zA-Z0-9]+}")
    public Object getStatisticForUser(@PathVariable String accountId) {
        String providedAccountId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!accountId.equals(providedAccountId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return urlMappingService.getStatisticForUser(accountId);
    }

    /**
     * Mapping which redirects from short url to the long url. Every existing path is publicly available.
     * Registered users can create short urls and then share them amongst community. It is not needed to
     * be registered in order to use the core functionality of this web application.
     * @param shortUrlIdentifier Short url (exactly six alphanumeric characters) which redirects to long.
     * @param response <code>HttpServletResponse</code> used for redirection.
     */
    @GetMapping("/{shortUrlIdentifier:[a-zA-Z0-9]+}")
    public void redirectUrl(@PathVariable String shortUrlIdentifier, HttpServletResponse response) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingService.findForRedirect(shortUrlIdentifier);
        if(optionalUrlMapping.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            UrlMapping urlMapping = optionalUrlMapping.get();
            response.setStatus(urlMapping.getRedirectType());
            response.setHeader(LOCATION, urlMapping.getUrl());
        }
    }
}
