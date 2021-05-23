package vocanec.marino.urlshortener.services;

import vocanec.marino.urlshortener.models.Account;
import vocanec.marino.urlshortener.responses.AccountResponse;

/**
 * Interface for AccountService component.
 */
public interface AccountService {

    /**
     * Creates new user account. Provided <code>accountId</code> must contain only alphanumeric characters, must
     * be unique and cannot be longer than 100 characters.
     * @param account Account details. Must have only one attribute set: <code>accountId</code>.
     * @return <code>AccountResponse</code> object which contains information about success of creation.
     */
    AccountResponse createAccount(Account account);

    /**
     * Checks whether provided <code>password</code> matches stored password for <code>accountId</code>.
     * @param accountId Account id for which check is performed.
     * @param password Provided password.
     * @return True if <code>password</code> matches stored password for <code>accountId</code>, false otherwise.
     */
    boolean authenticate(String accountId, String password);
}
