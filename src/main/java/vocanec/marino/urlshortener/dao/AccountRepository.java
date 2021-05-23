package vocanec.marino.urlshortener.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vocanec.marino.urlshortener.models.Account;

/**
 * Account repository (DAO) interface.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}
