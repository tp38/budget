package fr.teepi38.budget.database;


import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.teepi38.budget.accounts.AccountSummary;

public class RelaxDbTests {
    private ObjectMapper om = new ObjectMapper();
    private String auth_string = "Basic " + Base64.getEncoder().encodeToString( "couchdb:couchdb".getBytes() );
    private MockServerClient client = new MockServerClient("localhost", 1080);
    private RelaxDb db;


    @BeforeEach
    protected void onSetUp() {

        try {
            db = RelaxDb.builder()
                .url(new URI("http://localhost:"+client.getPort()+"/budget"))
                .credential(new Credential("couchdb", "couchdb"))
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
        assertEquals("http://localhost:"+client.getPort()+"/budget", db.url().toString() );
    }

    @Test
    // @Disabled
    public void have_a_getSummaryForAccount_method() {
        Calendar today = Calendar.getInstance();

        String[] key = { "LaBP_LEP_Th" };
        Float[] value = { 6245.27f, 8725.69f, 12485.10f};

        AccountSummary r = AccountSummary.builder().key( key ).value( value ).build();

        AccountSummary[] rr = new AccountSummary[1];
        rr[0] = r;

        HashMap<String,AccountSummary[]> hashmap = new HashMap<>();
        hashmap.put("rows", rr); 

        String body = null;
        try {
            body = om.writeValueAsString( hashmap );
        } catch (Exception e) {
            fail( "Exception occurs : " + e.getMessage() );
        }

        // set mock server
        String start_key = String.format( "[\"%s\",\"%d\",\"%d\"]", key[0], 2023, 12 );
        String end_key = String.format(  "[\"%s\",\"%d\",\"%d\"]", key[0], today.get(Calendar.YEAR), today.get(Calendar.MONTH)+1 );
        client
            .when( 
                request()
                    .withPath("/budget/_design/transferts/_view/by_endpoints")
                    .withHeaders( header( "Authorization", auth_string ) )
                    .withQueryStringParameter("start_key", start_key )
                    .withQueryStringParameter("end_key", end_key )
                    .withQueryStringParameter("reduce", "true" )
                    .withQueryStringParameter("group_level", "1" )
                    .withQueryStringParameter("inclusive_end", "true" )
            ).respond( 
                response().withStatusCode(201).withBody( body ) 
            );

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

        HashMap<String,Object> hm = new HashMap<>();
        hm.put("total_rows", expected_account_list.length );
        hm.put("offset", 0);
        hm.put("rows", expected_account_list ); 

        String body = null;
        try {
            body = om.writeValueAsString( hm );
        } catch (Exception e) {
            fail( "Exception occurs : " + e.getMessage() );
        }

        // set mock server
        client
            .when( 
                request()
                    .withPath("/budget/_design/endpoints/_view/family")
                    .withHeaders( header( "Authorization", auth_string ) )
            ).respond( 
                response().withStatusCode(201).withBody( body ) 
            );

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
