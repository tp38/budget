package fr.teepi38.budget.database;

import java.net.URI;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * a class to access to CouchDb Database with it's replication documents
 * @author : <a href="mailto:thierry.probst@free.fr">Thierry Probst</a>
 * @version 0.0, 29/03/2026
 */
@Data
@Builder
@Jacksonized @Accessors( fluent = true )
public class RelaxDb {
    private URI url;
    private Credential credential;

    /**
     * get a short string for the URI
     * @return a short string
     * @throws java.lang.NullPointerException
     */
    @JsonIgnore
    public String summary() throws NullPointerException {
        if ( url == null ) throw new NullPointerException( "url is null ");
        return url.getHost() + " (" + url.getPath().substring(1) + ")";
    }

}