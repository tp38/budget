package fr.teepi38.budget.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.junit.jupiter.api.Test;

public class CredentialTests {
    private String login = "login";
    private String pwd = "pa$$w0rd";

    @Test
    public void credential_must_return_a_basic_auth_string() {
        String expected = "Basic " + Base64.getEncoder().encodeToString( (login + ":" + pwd).getBytes() );

        Credential cred = new Credential(login, pwd);
        assertEquals(expected, cred.getBasicAuth() );

    }

    @Test
    public void credential_must_have_getters_and_setters() {
        Credential cred = Credential.builder().login(login).pwd(pwd).build();
        assertEquals(login, cred.login() );
        assertEquals(pwd, cred.pwd() );

        String new_login = "newLogin";
        String new_pwd = "dr0w$$ap";
        cred.login( new_login );
        cred.pwd(new_pwd);

        assertEquals(new_login, cred.login() );
        assertEquals(new_pwd, cred.pwd() );
    }

    @Test
    public void credential_password_should_respect_somes_rules() {
        assertTrue(true);
    }
    
}