package vocanec.marino.urlshortener.services.urlgen;

import vocanec.marino.urlshortener.dao.UrlMappingRepository;
import vocanec.marino.urlshortener.models.UrlMapping;

/**
 * Strategy (code design pattern) for generating short url identifiers. Since this applications
 * considers different approaches for generating identifiers it is good practice to define an
 * interface in order to provide alternate ways of executing some concrete logic.
 */
public interface ShortUrlIdentifierGeneratorStrategy {

    /**
     * Generates short url identifier and saves given <code>urlMapping</code> to the data storage.
     * @param urlMapping <code>UrlMapping</code> object for which short url identifier is being generated.
     * @param urlMappingRepository <code>Repository</code> for saving newly created <code>urlMapping</code>.
     */
    void generateShortUrlIdentifier(UrlMapping urlMapping, UrlMappingRepository urlMappingRepository);
}
