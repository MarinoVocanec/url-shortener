package vocanec.marino.urlshortener.services.urlgen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vocanec.marino.urlshortener.dao.UrlMappingRepository;
import vocanec.marino.urlshortener.models.UrlMapping;
import vocanec.marino.urlshortener.utility.AlphanumericEncoder;

/**
 * One implementation of interface {@link ShortUrlIdentifierGeneratorStrategy} which generates
 * random short url identifiers by encoding current <code>urlMapping</code> id into alphanumeric
 * short url identifier. Since this algorithm is generating new url identifier by simply incrementing
 * previous one, secure random preamble of fixed length is prepended to the identifier in order to
 * prevent simple iteration over all generated short url identifiers. This implementation is more
 * efficient than {@link RandomShortUrlIdentifierGeneratorStrategy} since it does not rely on pure
 * luck for generating non-seen short url identifier. However, it is worth stating that this implementation
 * gives somewhat predictable outputs for new identifiers (even though random preamble is present).
 * Additional advantage of this implementation is that space of possible short url identifiers cannot
 * be exhausted since it is progressively incrementing length of output identifiers.
 */
@Component
public class SequentialShortUrlIdentifierGeneratorStrategy implements ShortUrlIdentifierGeneratorStrategy {

    /**
     * Length of a secure preamble which is prepended to the encoder output.
     */
    private static final int PREAMBLE_LENGTH = 4;

    /**
     * Component for encoding current <code>urlMapping</code> id into alphanumeric short url identifier.
     */
    @Autowired
    private AlphanumericEncoder alphanumericEncoder;

    /**
     * Generates new random short url identifier. Random identifiers are generated by encoding provided
     * <code>urlMapping</code> id to base62 (alphanumeric characters) and prepending secure random preamble
     * of fixed length in order to prevent iteration over all generated short url identifiers.
     * @param urlMapping <code>UrlMapping</code> object for which short url identifier is being generated.
     * @param urlMappingRepository <code>Repository</code> for saving newly created <code>urlMapping</code>.
     */
    @Override
    public void generateShortUrlIdentifier(UrlMapping urlMapping, UrlMappingRepository urlMappingRepository) {
        urlMappingRepository.save(urlMapping);
        int nextId = urlMapping.getId();
        urlMapping.setShortUrlIdentifier(alphanumericEncoder.encode(nextId, PREAMBLE_LENGTH));
        urlMappingRepository.save(urlMapping);
    }
}
