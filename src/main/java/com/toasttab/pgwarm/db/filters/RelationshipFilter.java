package com.toasttab.pgwarm.db.filters;

import com.toasttab.pgwarm.db.DatabaseRelationship;

public interface RelationshipFilter {

    /**
     * determines the eligibility of any given relationship for inclusion
     * @param relation
     * @return true if the relationship satisfies given criteria, false otherwise
     */
    public boolean filter(DatabaseRelationship relation);
}
