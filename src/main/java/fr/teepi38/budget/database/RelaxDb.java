package fr.teepi38.budget.database;

import java.net.URI;
import java.util.Calendar;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.teepi38.budget.accounts.AccountSummary;

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

    /**
     * Retrieve list of accounts by queyring couchdb server at URL :
     * http://yoda:5984/budget/_design/endpoints/_view/family
     *
     * The couchdb server produce response body like 
     * {
     *    "total_rows": 7,
     *    "offset": 0,
     *    "rows": [
     *       {
     *          "id": "BSA_CC_Nat",
     *          "key": "BSA_CC_Nat",
     *          "value": "0000-00-00"
     *        },
     *        ...
     *        {
     *           "id": "LaBP_LEP_Th",
     *           "key": "LaBP_LEP_Th",
     *           "value": "2024-05-01"
     *        }
     *     ]
     * }
     * @return an arrays of Result object (id, key, value)
     * @throws Exception
     */
    @JsonIgnore
    public Result[] getAccountList() throws Exception {
        HttpUrl b = HttpUrl.parse(url.toString() + "/_design/endpoints/_view/family");
        if( b != null ) {
            HttpUrl.Builder urlBuilder = b.newBuilder();
            String u = urlBuilder.build().toString();
            Request request = new Request.Builder()
                .url( u )
                .header("Authorization", credential.getBasicAuth() )
                .build();
            
            OkHttpClient client = new OkHttpClient.Builder().build();

            Response response = client.newCall(request).execute();

            ObjectMapper mapper = new ObjectMapper();
            ResponseBody rbody = response.body();
            if( rbody != null ) {
                String body = rbody.string();
                JsonNode nodes = mapper.readTree( body );

                if( nodes.isObject() ) {
                    JsonNode len_nodes = nodes.get( "total_rows" );
                    int len = len_nodes.asInt();
                    Result[] result = new Result[ len ];

                    JsonNode rows = nodes.get( "rows" );
                    int i = 0;
                    for( JsonNode r : rows ) {
                        Result a = Result.builder()
                            .id(r.get("id").asText() )
                            .key( r.get("key").asText() )
                            .value( r.get("value").asText() )
                            .build();
                        result[i] = a;
                        i ++;
                    }
                    return result;
                }
            }

        }
        return null;
    }

    /**
     * Retrieve summary of an accounts by queyring couchdb server at URL :
     * http://yoda:5984/budget/_design/transferts/_view/by_endpoints?
     * inclusive_end=true&
     * start_key=%5B%22LaBP_CC_Joint%22%2C%222023%22%2C%2212%22%5D&
     * end_key=%5B%22LaBP_CC_Joint%22%2C%222026%22%2C%2203%22%5D&
     * reduce=true&
     * group_level=1
     * server produce a response like 
     * {
     *   "rows": [
     *     {
     *       "key": [
     *         "LaBP_CC_Joint"
     *       ],
     *       "value": [
     *         179.7399999999982,
     *         0,
     *         0
     *       ]
     *     }
     *   ]
     * }
     * @param account a string that representant the account name
     * @param day the day for the end key
     * @return an AccountSummary Object
     * @throws Exception
     */
    @JsonIgnore
    public AccountSummary getSummaryForAccount( String account, Calendar day ) throws Exception {
        HttpUrl a = HttpUrl.parse(url.toString() + "/_design/transferts/_view/by_endpoints");
        if( a != null ) {
            HttpUrl.Builder urlBuilder = a.newBuilder();
            urlBuilder.addQueryParameter("start_key", String.format( "[\"%s\",\"%d\",\"%d\"]", account, 2023, 12 ) );
            urlBuilder.addQueryParameter("end_key", String.format(  "[\"%s\",\"%d\",\"%d\"]", account, day.get(Calendar.YEAR), day.get(Calendar.MONTH)+1 ) );
            urlBuilder.addQueryParameter("reduce", "true");
            urlBuilder.addQueryParameter("group_level", "1");
            urlBuilder.addQueryParameter("inclusive_end", "true");

            String u = urlBuilder.build().toString();

            Request request = new Request.Builder()
                .url( u )
                .header("Authorization", credential.getBasicAuth() )
                .build();
            
            OkHttpClient client = new OkHttpClient.Builder().build();

            Response response = client.newCall(request).execute();

            ObjectMapper mapper = new ObjectMapper();
            ResponseBody rbody = response.body();
            if( rbody != null ) {
                String body = rbody.string();
                JsonNode nodes = mapper.readTree( body );

                if( nodes.isObject() ) {
                    JsonNode rows = nodes.get("rows");
                    if( rows.isArray() ) {
                        AccountSummary as = null;
                        for( JsonNode r : rows ) {
                            String[] key = { r.get( "key").get(0).asText() };
                            Float[] value = { 
                                r.get( "value" ).get(0).floatValue(), 
                                r.get( "value" ).get(1).floatValue(), 
                                r.get( "value" ).get(2).floatValue() };
                            as = AccountSummary.builder().key(key).value(value).build();
                        }
                        return as;
                    }
                }
            }
        }
        return null;

    }
}