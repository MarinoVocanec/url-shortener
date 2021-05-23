package vocanec.marino.urlshortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vocanec.marino.urlshortener.models.Account;
import vocanec.marino.urlshortener.responses.AccountResponse;
import vocanec.marino.urlshortener.services.AccountService;

/**
 * Controller which contains exactly one <b>POST</b> mapping responsible for creating new user account.
 */
@RestController
public class AccountController {

    /**
     * Account service which contains business logic for creating new user account.
     */
    @Autowired
    private AccountService accountService;

    /**
     * Mapping for account creation.
     * @param account RequestBody object which must have exactly one property set <code>accountId</code>.
     * @return <code>AccountResponse</code> object which encapsulates all information about success of the call.
     */
    @PostMapping("/account")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody Account account) {
        AccountResponse response = accountService.createAccount(account);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
}
