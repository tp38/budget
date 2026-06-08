package fr.teepi38.budget.database;

import java.util.Base64;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import lombok.Builder;

/**
 * Credential encapsulate login and password used to connect to a database.
 *
 * @author : <a href="mailto:thierry.probst@free.fr">Thierry Probst</a>
 * @version 0.0 08/06/2026
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
@Jacksonized @Accessors( fluent = true )
public class Credential {
    private String login;
    private String pwd;

    /**
     * test if a string can become a valid login
     * @param tested_login the string to test
     * @return a boolean 
     */
    public static boolean isLoginValid( String tested_login ) {
        if( ! tested_login.equals( "" ) ) {
            return true;
        }
        return false;
    }

    /**
     * test if a string can become a valid password
     * @param tested_password the string to test
     * @return a boolean
     */
    public static boolean isPasswordValid( String tested_password ) {
        if( ! tested_password.equals( "" ) ) {
            return true;
        }
        return false;
    }

    /**
     * return a basic authentication string for http
     * @return a string with the encoded login and password
     */
    public String getBasicAuth() {
        return "Basic " + Base64.getEncoder().encodeToString( (login+":"+pwd).getBytes());   
    }
}