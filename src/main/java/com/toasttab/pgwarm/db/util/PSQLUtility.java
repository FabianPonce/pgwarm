package com.toasttab.pgwarm.db.util;

public class PSQLUtility {
    /**
     * Returns true if the SQLState passed is one that can be safely retried, or false if it would likely
     * just continue to fail.
     *
     * This method is designed to allow for the retrying of queries that would otherwise succeed, for example,
     * due to replication delay or otherwise.
     *
     * See: https://www.postgresql.org/docs/9.2/static/errcodes-appendix.html
     */
    public static boolean isRetriableSqlState(String sqlState) {
        if(sqlState.equals("40001")) // cancelled due to conflict with recovery
            return true;

        return false;
    }
}
