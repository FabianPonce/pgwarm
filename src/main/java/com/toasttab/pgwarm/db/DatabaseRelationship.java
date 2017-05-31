package com.toasttab.pgwarm.db;


public class DatabaseRelationship {
    private final String relname;
    private final String schema;
    private final RelationshipType type;

    public DatabaseRelationship(String relname, String schema, RelationshipType reltype) {
        this.relname = relname;
        this.schema = schema;
        this.type = reltype;
    }

    public final String getName() {
        return this.relname;
    }

    public final String getSchema() {
        return this.schema;
    }

    public final RelationshipType getRelationshipType() {
        return this.type;
    }
}
