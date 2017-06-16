package com.toasttab.pgwarm.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RelationshipFactory {
    private final Map<String, Relationship> relNameMap;
    public RelationshipFactory() {
        this.relNameMap = new HashMap<String, Relationship>();
    }

    public Relationship newIndex(String schema, String name, String table) {
        TableRelationship sourceTable = (TableRelationship)relNameMap.get(String.format("%s.\"%s\"", schema, table));
        IndexRelationship relationship = new IndexRelationship(schema, name, sourceTable);

        sourceTable.addRelatedIndex(relationship);

        relNameMap.put(relationship.toString(), relationship);
        return relationship;
    }

    public Relationship newTable(String schema, String name) {
        TableRelationship relationship = new TableRelationship(schema, name);
        relNameMap.put(relationship.toString(), relationship);
        return relationship;
    }

    public Collection<Relationship> getRelations() {
        return relNameMap.values();
    }

}
