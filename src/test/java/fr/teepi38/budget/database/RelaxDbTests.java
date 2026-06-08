package fr.teepi38.budget.database;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RelaxDbTests {
    private RelaxDb db;

    @BeforeEach
    protected void onSetUp() {
        try {
            db = RelaxDb.builder()
                .url(new URI("http://localhost:5984/budget"))
                .credential(new Credential("nologin", "nopassword"))
                .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
       
    @Test
    public void have_a_summary_method() {
        assertEquals("localhost (budget)", db.summary() );
    }

    @Test
    public void have_a_get_url_method() {
        assertEquals("http://localhost:5984/budget", db.url().toString() );
    }

}
