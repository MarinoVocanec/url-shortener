package vocanec.marino.urlshortener.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vocanec.marino.urlshortener.models.UrlMapping;

import java.util.Optional;

/**
 * Url mapping repository (DAO) interface.
 */
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Integer> {

    /**
     * Checks whether there already exists url mapping with the given <code>shortUrlIdentifier</code>.
     * Since short url identifier is unique this check must be performed when new url mapping is being crated.
     * @param shortUrlIdentifier Short url identifier - 6 characters long alphanumeric string.
     * @return True if given <code>shortUrlIdentifier</code> does not already exist in the database, false otherwise.
     */
    boolean existsByShortUrlIdentifier(String shortUrlIdentifier);

    /**
     * Retrieves <code>Optional</code> Url mapping for given <code>shortUrlIdentifier</code>.
     * @param shortUrlIdentifier Short url identifier for possible stored <code>UrlMapping</code>.
     * @return <code>Optional</code> Url mapping (present if mapping was found for <code>shortUrlIdentifier</code>).
     */
    Optional<UrlMapping> findByShortUrlIdentifier(String shortUrlIdentifier);

    /**
     * Checks whether there already exists a mapping with provided <code>url</code> and <code>accountId</code>.
     * Since we need the statistic for each user in a map form, no duplicate long urls are allowed for particular user.
     * Therefore, this method allows us to check for possible collisions.
     * @param accountId Account identifier (username).
     * @param url Long url which is being checked for possible collision with other user's long urls.
     * @return <code>Optional</code> Url mapping (present if collision was found).
     */
    Optional<UrlMapping> findByAccountIdAndUrl(String accountId, String url);
}
