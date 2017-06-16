package com.toasttab.pgwarm.db.filters;

import com.toasttab.pgwarm.db.Relationship;

import java.util.List;

public class RelationshipNameFilter implements RelationshipFilter {
    public final List<String> names;

    public RelationshipNameFilter(List<String> names) {
        this.names = names;
    }

    public boolean filter(Relationship relation) {
        for(String name : names) {
            if(relation.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
}
