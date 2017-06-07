package com.toasttab.pgwarm.db;

public enum PrewarmMode {
    PREFETCH,
    READ,
    BUFFER;

    public final String toSqlArgument() {
        return this.toString().toLowerCase();
    }
}
