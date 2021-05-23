package vocanec.marino.urlshortener.services.urlgen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vocanec.marino.urlshortener.dao.UrlMappingRepository;
import vocanec.marino.urlshortener.models.UrlMapping;
import vocanec.marino.urlshortener.utility.RandomAlphanumericGenerator;

/**
 * One implementation of interface {@link ShortUrlIdentifierGeneratorStrategy} which generates
 * random short url identifiers of fixed length. Although this allows completely random urls it
 * is rather inefficient. After concrete short url identifier is generated there is a need to
 * check whether duplicate already exists in the database. For that reason this implementation
 * is not very scalable - when space of possible short url identifiers shrinks, generating new
 * short url identifiers become longer as there is a much greater probability for a collision.
 * What is more, it is possible (but very unlikely i.e. 62<sup>6</sup> = 56,800,235,584) that
 * all combinations are exhausted in which case new url cannot be generated.
 */
@Component
public class RandomShortUrlIdentifierGeneratorStrategy implements ShortUrlIdentifierGeneratorStrategy {

    /**
     * Length for a short url identifier.
     */
    private static final int SHORT_URL_IDENTIFIER_LENGTH = 6;

    /**
     * Component for generating random 6 characters long alphanumeric short url identifiers.
     */
    @Autowired
    private RandomAlphanumericGenerator randomAlphanumericGenerator;

    /**
     * Generates new random short url identifier. Random identifiers are generated iteratively until non-existing
     * short url identifier is found. Implementation is simple, but rather inefficient and not scalable.
     * @param urlMapping <code>UrlMapping</code> object for which short url identifier is being generated.
     * @param urlMappingRepository <code>Repository</code> for saving newly created <code>urlMapping</code>.
     */
    @Override
    public void generateShortUrlIdentifier(UrlMapping urlMapping, UrlMappingRepository urlMappingRepository) {
        while(true) {
            String shortUrlIdentifier = randomAlphanumericGenerator.generateAlphanumericString(SHORT_URL_IDENTIFIER_LENGTH);
            if(!urlMappingRepository.existsByShortUrlIdentifier(shortUrlIdentifier)) {
                urlMapping.setShortUrlIdentifier(shortUrlIdentifier);
                urlMappingRepository.save(urlMapping);
                return;
            }
        }
    }
}
