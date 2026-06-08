package fr.teepi38.budget.accounts;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountSummaryTests {
    private AccountSummary r;
    private String[] key = { "this is a key" };
    private Float[] val = { Float.valueOf(17.22f), Float.valueOf(42.12f), Float.valueOf(0.0f) };

    @BeforeEach
    void onSetUp() {
        r = AccountSummary.builder()
            .key( key )
            .value( val )
            .build();
    }

    @Test
    void should_have_a_get_key_method() {
        assertTrue( r.key().equals(  key ));
    }

    @Test
    void should_have_a_get_value_method() {
        assertTrue( r.value().equals( val ) );
    }
}
