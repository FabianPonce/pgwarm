package com.toasttab.pgwarm.db.filters;

import com.toasttab.pgwarm.db.Relationship;

import java.util.List;

public class RelationshipSchemaFilter implements RelationshipFilter {
    private final List<String> schemas;

    public RelationshipSchemaFilter(List<String> schemas) {
        this.schemas = schemas;
    }

    public boolean filter(Relationship relation) {
        return (schemas.contains(relation.getSchema()));
    }
}
