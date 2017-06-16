package com.toasttab.pgwarm.db;

public class IndexRelationship extends Relationship {
    private final TableRelationship sourceTable;
    public IndexRelationship(String schema, String name, TableRelationship sourceTable) {
        super(name, schema, RelationshipType.INDEX);
        this.sourceTable = sourceTable;
    }

    public TableRelationship getSourceTable() {
        return this.sourceTable;
    }
}
