package vocanec.marino.urlshortener.services.impl;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vocanec.marino.urlshortener.dao.UrlMappingRepository;
import vocanec.marino.urlshortener.models.UrlMapping;
import vocanec.marino.urlshortener.responses.UrlMappingResponse;
import vocanec.marino.urlshortener.services.UrlMappingService;
import vocanec.marino.urlshortener.services.urlgen.ShortUrlIdentifierGeneratorStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of a <code>UrlMappingService</code> interface.
 */
@Service
public class UrlMappingServiceImpl implements UrlMappingService {

    /**
     * Repository (DAO) for <code>UrlMapping</code> data manipulation.
     */
    @Autowired
    private UrlMappingRepository urlMappingRepository;

    /**
     * Strategy for generating random short url identifiers.
     * For demonstration purposes random generator was chosen.
     */
    @Autowired
    private ShortUrlIdentifierGeneratorStrategy randomShortUrlIdentifierGeneratorStrategy;

    /**
     * <code>UrlValidator</code> in order to check if provided url is actually an url.
     */
    private final UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

    /**
     * {@inheritDoc}
     */
    @Override
    public UrlMappingResponse createUrlMapping(String accountId, UrlMapping urlMapping) {
        String url = urlMapping.getUrl();

        if(url == null || url.isBlank()) {
            return createFailResponse("url was not provided.");
        }

        if(!urlValidator.isValid(url)) {
            return createFailResponse("Provided input is not a valid url. Was: " + url);
        }

        Integer redirectType = urlMapping.getRedirectType();
        if(redirectType != null && redirectType != 301 && redirectType != 302) {
            return createFailResponse("Redirect type must be 301 or 302. Was: " + redirectType);
        }

        Optional<UrlMapping> existingUrlMapping = urlMappingRepository.findByAccountIdAndUrl(accountId, url);
        if(existingUrlMapping.isPresent()) {
            String shortUrl = buildShortUrl(existingUrlMapping.get().getShortUrlIdentifier());
            return new UrlMappingResponse(false, shortUrl, "Mapping for given URL already exists.");
        }

        urlMapping.setAccountId(accountId);
        urlMapping.setRedirectType(redirectType != null ? redirectType : 302);
        urlMapping.setVisitCounter(0);
        randomShortUrlIdentifierGeneratorStrategy.generateShortUrlIdentifier(urlMapping, urlMappingRepository);

        String shortUrlIdentifier = urlMapping.getShortUrlIdentifier();
        return new UrlMappingResponse(true, buildShortUrl(shortUrlIdentifier), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> getStatisticForUser(String accountId) {
        return urlMappingRepository.findAll().stream()
                .filter(u -> u.getAccountId().equals(accountId))
                .collect(Collectors.toMap(UrlMapping::getUrl, UrlMapping::getVisitCounter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UrlMapping> findForRedirect(String shortUrlIdentifier) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrlIdentifier(shortUrlIdentifier);
        if(optionalUrlMapping.isPresent()) {
            UrlMapping urlMapping = optionalUrlMapping.get();
            urlMapping.setVisitCounter(urlMapping.getVisitCounter() + 1);
            urlMappingRepository.save(urlMapping);
        }
        return optionalUrlMapping;
    }

    /**
     * Builds short url for a given <code>shortUrlIdentifier</code>. This is created so that
     * base url does not depend on where application is hosted or deployed.
     * @param shortUrlIdentifier Short url identifier for which short url is built.
     * @return Short url.
     */
    private String buildShortUrl(String shortUrlIdentifier) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/" + shortUrlIdentifier;
    }

    /**
     * Creates <code>UrlMappingResponse</code> with a provided error message.
     * @param message Error message.
     * @return <code>UrlMappingReposne</code> with <code>success</code> set to false,
     * <code>shortUrl</code> to null and with proper error message.
     */
    private UrlMappingResponse createFailResponse(String message) {
        return new UrlMappingResponse(false,null, message);
    }
}
