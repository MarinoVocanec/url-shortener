package vocanec.marino.urlshortener.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Model for database entity UrlMapping.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {

    /**
     * Unique auto generated identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Foreign key. Each url mapping belongs to exactly one account.
     */
    // Take a note how we could have used @ManyToOne with proper Account here.
    // However, for the simplicity of the provided solution this was purposely omitted.
    @NotNull
    private String accountId;

    /**
     * Long url where user is redirected upon accessing short url.
     */
    // Column is set to LONGTEXT as URLs might be very long.
    @Column(columnDefinition = "LONGTEXT")
    private String url;

    /**
     * Short url identifier used for generating short url.
     */
    @NotNull
    @Column(unique = true)
    private String shortUrlIdentifier;

    /**
     * Redirect type (HTTP status code) which has exactly two legal values:
     * <ul>
     *     <li>301 (Moved Permanently)</li>
     *     <li>302 (Found)</li>
     * </ul>
     */
    @NotNull
    @Min(301)
    @Max(302)
    private Integer redirectType = 302;

    /**
     * Integer value which represents how many times short url was accessed.
     */
    @NotNull
    private Integer visitCounter;
}
