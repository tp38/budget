package fr.teepi38.budget.database;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.teepi38.budget.accounts.AccountSummary;

public class RelaxDbTests {
    // mockserver host
    private String ms_host = "localhost";
    // mockserver port
    private String ms_port = "1080";
    private RelaxDb db;


    @BeforeEach
    protected void onSetUp() {

        try {
            db = RelaxDb.builder()
                .url(new URI("http://"+ms_host+":"+ms_port+"/budget"))
                .credential(new Credential("couchdb", "couchdb"))
                .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
       
    @Test
    public void have_a_summary_method() {
        assertEquals(ms_host+" (budget)", db.summary() );
    }

    @Test
    public void have_a_get_url_method() {
        assertEquals("http://"+ms_host+":"+ms_port+"/budget", db.url().toString() );
    }

    @Test
    // @Disabled
    public void have_a_getSummaryForAccount_method() {
        Calendar today = Calendar.getInstance();

        String[] key = { "LaBP_LEP_Th" };
        Float[] value = { 6245.27f, 8725.69f, 12485.10f};

        AccountSummary expected_as = AccountSummary.builder().key( key ).value(value).build();
        try {
            AccountSummary as = db.getSummaryForAccount( "LaBP_LEP_Th", today );
            assertEquals(expected_as, as);
        } catch( Exception e ) {
            fail( "Exception occurs : " + e.getMessage() );
        }
    }

    @Test
    // @Disabled
    public void have_a_getaccountlist_method() {
        String[] k = { "BSA_CC_Nat" };
        String d = "0000-00-00";

        String[] k1 = { "BSA_CC_Th" };
        String d1 = "0000-00-00";

        String[] k2 = { "LaBP_LEP_Th" }; 
        String d2 ="2024-05-01";

        Result r = Result.builder().id( k[0] ).key( k[0] ).value( d ).build();
        Result r1 = Result.builder().id( k1[0] ).key( k1[0] ).value( d1 ).build();
        Result r2 = Result.builder().id( k2[0] ).key( k2[0] ).value( d2 ).build();

        Result[] expected_account_list = new Result[] { r, r1, r2 };
        try {
            Result[] list = db.getAccountList();
            assertEquals(expected_account_list.length, list.length);
            for( int i=0; i< list.length; i++ ) {
                assertEquals(expected_account_list[i], list[i]);
            }
        } catch( Exception e ) {
            fail( "Exception occurs : " + e.getMessage() );
        }
    } 

}
