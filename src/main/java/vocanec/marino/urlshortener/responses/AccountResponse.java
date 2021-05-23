package vocanec.marino.urlshortener.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a class for an account creation response. Given properties
 * are serialized and returned to the API caller.
 */
@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AccountResponse {

    /**
     * Boolean flag which represents whether account was successfully created.
     */
    private final boolean success;

    /**
     * String with a description of the account creation outcome.
     */
    private final String description;

    /**
     * This property is set if and only if account was successfully created.
     * Represents password for the newly created account and it is always 8 characters
     * long alphanumeric string.
     */
    private final String password;
}
