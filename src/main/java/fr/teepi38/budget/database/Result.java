package fr.teepi38.budget.database;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized @Accessors( fluent = true )
public class Result {
    private String id;
    private String key;
    private String value;
}
