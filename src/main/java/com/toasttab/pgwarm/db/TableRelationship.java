package com.toasttab.pgwarm.db;

import java.util.ArrayList;
import java.util.Collection;

public class TableRelationship extends Relationship {
    private final Collection<IndexRelationship> relatedIndices;

    public TableRelationship(String schema, String name) {
        super(name, schema, RelationshipType.TABLE);

        this.relatedIndices = new ArrayList<IndexRelationship>();
    }

    public Collection<IndexRelationship> getRelatedIndices() {
        return relatedIndices;
    }

    public void addRelatedIndex(IndexRelationship relation) {
        relatedIndices.add(relation);
    }
}
