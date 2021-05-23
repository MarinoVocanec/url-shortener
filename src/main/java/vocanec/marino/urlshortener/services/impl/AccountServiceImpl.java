package vocanec.marino.urlshortener.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vocanec.marino.urlshortener.auth.PasswordEncoder;
import vocanec.marino.urlshortener.dao.AccountRepository;
import vocanec.marino.urlshortener.models.Account;
import vocanec.marino.urlshortener.responses.AccountResponse;
import vocanec.marino.urlshortener.services.AccountService;
import vocanec.marino.urlshortener.utility.RandomAlphanumericGenerator;

import java.util.Optional;

/**
 * Implementation of an <code>AccountService</code> interface.
 */
@Service
public class AccountServiceImpl implements AccountService {

    /**
     * Length of a server generated alphanumeric password.
     */
    private static final int PASSWORD_LENGTH = 8;

    /**
     * Maximum possible length for an <code>accountId</code> (that is username).
     */
    private static final int ACCOUNT_ID_MAX_LENGTH = 100;

    /**
     * Repository (DAO) for <code>Account</code> data manipulation.
     */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Password encoder for hashing passwords and checking provided passwords
     * upon login with the ones stored in the database.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Component for generating random 8 characters long alphanumeric passwords.
     */
    @Autowired
    private RandomAlphanumericGenerator randomAlphanumericGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountResponse createAccount(Account account) {
        String accountId = account.getAccountId();

        if(accountId == null || accountId.isBlank()) {
            return createFailResponse("accountId was not provided.");
        }

        if(accountId.length() > ACCOUNT_ID_MAX_LENGTH) {
            return createFailResponse(
                    String.format("accountId length cannot be longer than %d, was: %d.",
                            ACCOUNT_ID_MAX_LENGTH, accountId.length())
            );
        }

        if(!accountId.matches("[a-zA-Z0-9]+")) {
            return createFailResponse("accountId can contain only alphanumeric characters.");
        }

        if(accountRepository.existsById(accountId)) {
            return createFailResponse("Account with accountId: '" + accountId + "' already exists.");
        }

        String password = randomAlphanumericGenerator.generateAlphanumericString(PASSWORD_LENGTH);
        account.setPasswordHash(passwordEncoder.encode(password));
        accountRepository.save(account);
        return new AccountResponse(true, "Your account is opened.", password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean authenticate(String accountId, String password) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()) return false;

        return passwordEncoder.matches(password, optionalAccount.get().getPasswordHash());
    }

    /**
     * Creates <code>AccountResponse</code> with a provided error message.
     * @param message Error message.
     * @return <code>AccountResponse</code> with <code>success</code> set to false,
     * <code>password</code> to null and with proper error message.
     */
    private AccountResponse createFailResponse(String message) {
        return new AccountResponse(false, message, null);
    }
}
