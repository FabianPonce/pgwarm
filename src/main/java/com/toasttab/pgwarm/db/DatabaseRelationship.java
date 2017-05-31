package com.toasttab.pgwarm.db;


public class DatabaseRelationship {
    private final String relname;
    private final String schema;
    public DatabaseRelationship(String relname, String schema) {
        this.relname = relname;
        this.schema = schema;
    }

    public final String getName() {
        return this.relname;
    }

    public final String getSchema() {
        return this.schema;
    }
}
