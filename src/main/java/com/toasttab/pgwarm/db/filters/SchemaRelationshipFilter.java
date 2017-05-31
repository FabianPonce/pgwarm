package com.toasttab.pgwarm.db.filters;

import com.toasttab.pgwarm.db.DatabaseRelationship;

import java.util.List;

public class SchemaRelationshipFilter implements RelationshipFilter {
    private final List<String> schemas;

    public SchemaRelationshipFilter(List<String> schemas) {
        this.schemas = schemas;
    }

    public boolean filter(DatabaseRelationship relation) {
        return (schemas.contains(relation.getSchema()));
    }
}
