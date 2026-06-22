package fr.teepi38.budget.database;


import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.teepi38.budget.accounts.AccountSummary;

@ExtendWith(MockServerExtension.class)
@MockServerSettings( ports = {8888} )
public class RelaxDbTests {
    private ObjectMapper om;
    private String auth_string;
    private Result[] expected_account_list;

    private RelaxDb db;
    private final ClientAndServer mock_server;


    public RelaxDbTests( ClientAndServer server ) throws Exception {
        this.mock_server = server;
        // mock_server.openUI(TimeUnit.SECONDS, 60);
        mock_server.openUI();

        this.om = new ObjectMapper();

        // create authentication string 
        this.auth_string = "Basic " + Base64.getEncoder().encodeToString( "couchdb:couchdb".getBytes() );
        
        String[] k = { "BSA_CC_Nat" };
        // Float[] v = { 12.69f, 15.21f, 0.0f };
        String d = "0000-00-00";
        // setMockServer4AccountSummary( k, v );

        String[] k1 = { "BSA_CC_Th" };
        // Float[] v1 = { 42.17f, 12.69f, 69.69f}; 
        String d1 = "0000-00-00";
        // setMockServer4AccountSummary( k1, v1 );

        String[] k2 = { "LaBP_LEP_Th" }; 
        Float[] v2 = { 6245.27f, 8725.69f, 12485.10f}; 
        String d2 ="2024-05-01";
        setMockServer4AccountSummary( k2, v2 );

        Result r = Result.builder().id( k[0] ).key( k[0] ).value( d ).build();
        Result r1 = Result.builder().id( k1[0] ).key( k1[0] ).value( d1 ).build();
        Result r2 = Result.builder().id( k2[0] ).key( k2[0] ).value( d2 ).build();

        expected_account_list = new Result[] { r, r1, r2 };
        setMockServer4AccountList( expected_account_list);

    }

    private void setMockServer4AccountList( Result[] ar ) throws Exception {
        // http://yoda:5984/budget/_design/endpoints/_view/family
        // produce response body like 
        // {
        // "total_rows": 7,
        // "offset": 0,
        // "rows": [
        //         {
        //         "id": "BSA_CC_Nat",
        //         "key": "BSA_CC_Nat",
        //         "value": "0000-00-00"
        //         },
        //         ...
        //         {
        //         "id": "LaBP_LEP_Th",
        //         "key": "LaBP_LEP_Th",
        //         "value": "2024-05-01"
        //         }
        //     ]
        // }
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("total_rows", ar.length );
        hm.put("offset", 0);
        hm.put("rows", ar); 

        String body = om.writeValueAsString( hm );

        // set mock server
        mock_server
            .when( 
                request()
                    .withPath("/budget/_design/endpoints/_view/family")
                    .withHeaders( header( "Authorization", auth_string ) )
            ).respond( 
                response().withStatusCode(201).withBody( body ) 
            );
    }

    private void setMockServer4AccountSummary( String[] k, Float[] v ) throws Exception {
        // http://yoda:5984/budget/_design/transferts/_view/by_endpoints?
        // inclusive_end=true&
        // start_key=%5B%22LaBP_CC_Joint%22%2C%222023%22%2C%2212%22%5D&
        // end_key=%5B%22LaBP_CC_Joint%22%2C%222026%22%2C%2203%22%5D&
        // reduce=true&
        // group_level=1
        // produce response like 
        // {
        //   "rows": [
        //     {
        //       "key": [
        //         "LaBP_CC_Joint"
        //       ],
        //       "value": [
        //         179.7399999999982,
        //         0,
        //         0
        //       ]
        //     }
        //   ]
        // }

        Calendar day = Calendar.getInstance();

        AccountSummary r = AccountSummary.builder().key(k).value( v ).build();

        AccountSummary[] rr = new AccountSummary[1];
        rr[0] = r;

        HashMap<String,AccountSummary[]> hashmap = new HashMap<>();
        hashmap.put("rows", rr); 

        String body = om.writeValueAsString( hashmap );

        // set mock server
        String start_key = String.format( "[\"%s\",\"%d\",\"%d\"]", k[0], 2023, 12 );
        String end_key = String.format(  "[\"%s\",\"%d\",\"%d\"]", k[0], day.get(Calendar.YEAR), day.get(Calendar.MONTH)+1 );
        mock_server
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
    
    }


    @BeforeEach
    protected void onSetUp() {
        try {
            db = RelaxDb.builder()
                .url(new URI("http://localhost:"+mock_server.getPort()+"/budget"))
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
        assertEquals("http://localhost:"+mock_server.getPort()+"/budget", db.url().toString() );
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
