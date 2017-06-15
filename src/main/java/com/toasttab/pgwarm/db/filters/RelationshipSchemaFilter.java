package com.toasttab.pgwarm.db.filters;

import com.toasttab.pgwarm.db.DatabaseRelationship;

import java.util.List;

public class RelationshipSchemaFilter implements RelationshipFilter {
    private final List<String> schemas;

    public RelationshipSchemaFilter(List<String> schemas) {
        this.schemas = schemas;
    }

    public boolean filter(DatabaseRelationship relation) {
        return (schemas.contains(relation.getSchema()));
    }
}