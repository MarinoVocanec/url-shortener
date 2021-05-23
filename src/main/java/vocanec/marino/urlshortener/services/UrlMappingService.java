package vocanec.marino.urlshortener.services;

import vocanec.marino.urlshortener.models.UrlMapping;
import vocanec.marino.urlshortener.responses.UrlMappingResponse;

import java.util.Map;
import java.util.Optional;

/**
 * Interface for UrlMappingService component.
 */
public interface UrlMappingService {

    /**
     * Creates new url mapping for given user with <code>accountId</code> and long <code>url</code>
     * in the <code>urlMapping</code>. Optional <code>responseType</code> can be provided in the
     * <code>urlMapping</code> as well but must be set to 301 or 302 (302 is default).
     * Url mapping can be created if and only if <code>url</code> and <code>responseType</code> are
     * valid and if long <code>url</code> is not already shortened for user with id <code>accountId</code>.
     * @param accountId Account id for which url mapping is created.
     * @param urlMapping Url mapping which contains long <code>url</code>.
     * @return <code>UrlMappingResponse</code> object which contains information about success of creation.
     */
    UrlMappingResponse createUrlMapping(String accountId, UrlMapping urlMapping);

    /**
     * Retrieves statistic for user with <code>accountId</code>. Statistic represents a map
     * of long urls as keys and number of visit counts as corresponding values.
     * @param accountId Account id for which statistic is retrieved.
     * @return Map with long URLs as keys and number of visit counts as values.
     */
    Map<String, Integer> getStatisticForUser(String accountId);

    /**
     * Gets url mapping for a given unique short url identifier (6 alphanumeric characters).
     * @param shortUrlIdentifier Unique short url identifier.
     * @return <code>Optional</code> Url mapping for a given unique short url identifier.
     */
    Optional<UrlMapping> findForRedirect(String shortUrlIdentifier);
}
