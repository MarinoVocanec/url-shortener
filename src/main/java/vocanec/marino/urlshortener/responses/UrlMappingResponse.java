package vocanec.marino.urlshortener.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a class for an url mapping creation response. Given properties
 * are serialized and returned to the API caller.
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlMappingResponse {

    /**
     * Boolean flag which represents whether account was successfully created.
     * Specification of this task does not mention anything about this property
     * so it is ignored upon serialization.
     */
    @JsonIgnore
    private final boolean success;

    /**
     * New created short url. It is set if new short url was created or if user
     * tried to create a mapping for already stored long url. In second case this
     * property can be handy for the frontend as it directly tells which short
     * url to use.
     */
    private final String shortUrl;

    /**
     * Error message describing which error occurred.
     */
    private final String errorMessage;
}
