package vocanec.marino.urlshortener.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;

/**
 * Model for database entity Account. Contains very basic information
 * about user account: <code>accountId</code> (which is actually username)
 * and <code>passwordHash</code>.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    /**
     * Account unique identifier (username).
     */
    @Id
    private String accountId;

    /**
     * Hash of a generated 8 characters long alphanumeric password.
     */
    @NotNull
    private String passwordHash;
}
