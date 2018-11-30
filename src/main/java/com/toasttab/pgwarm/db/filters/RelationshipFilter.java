package com.toasttab.pgwarm.db.filters;

import com.toasttab.pgwarm.db.Relationship;

public interface RelationshipFilter {

    /**
     * determines the eligibility of any given relationship for inclusion
     * @param relation
     * @return true if the relationship satisfies given criteria, false otherwise
     */
    boolean filter(Relationship relation);
}
