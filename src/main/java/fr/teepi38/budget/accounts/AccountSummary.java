package fr.teepi38.budget.accounts;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

/**
 * a class to access to CouchDb  Query Result
 * @author : <a href="mailto:thierry.probst@free.fr">Thierry Probst</a>
 * @version 0.0, 23/04/2026
 * @since 0.0.4
 */
@Data
@Builder
@Jacksonized @Accessors( fluent = true )
public class AccountSummary {
    private String[] key;
    private Float[] value;   
}
