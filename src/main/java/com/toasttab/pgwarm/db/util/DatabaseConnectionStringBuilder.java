package com.toasttab.pgwarm.db.util;

public class DatabaseConnectionStringBuilder {
    private String hostname = "";
    private String username = "";
    private String password = "";
    private int port        = 5432;
    private String database = "";

    public DatabaseConnectionStringBuilder() {
    }

    public DatabaseConnectionStringBuilder(DatabaseConnectionStringBuilder clone) {
        hostname = clone.hostname;
        username = clone.username;
        password = clone.password;
        port = clone.port;
        database = clone.database;
    }

    public final DatabaseConnectionStringBuilder withHostname(String hostname) {
        DatabaseConnectionStringBuilder builder = new DatabaseConnectionStringBuilder(this);
        builder.hostname = hostname;

        return builder;
    }

    public final DatabaseConnectionStringBuilder withUsername(String username) {
        DatabaseConnectionStringBuilder builder = new DatabaseConnectionStringBuilder(this);
        builder.username = username;

        return builder;
    }

    public final DatabaseConnectionStringBuilder withPassword(String password) {
        DatabaseConnectionStringBuilder builder = new DatabaseConnectionStringBuilder(this);
        builder.password = password;

        return builder;
    }

    public final DatabaseConnectionStringBuilder withPort(int port) {
        DatabaseConnectionStringBuilder builder = new DatabaseConnectionStringBuilder(this);
        builder.port = port;

        return builder;
    }

    public final DatabaseConnectionStringBuilder withDatabase(String database) {
        DatabaseConnectionStringBuilder builder = new DatabaseConnectionStringBuilder(this);
        builder.database = database;

        return builder;
    }

    public final String toString() {
        return String.format("jdbc:postgresql://%s:%s/%s?ssl=true&user=%s&password=%s&sslfactory=org.postgresql.ssl.NonValidatingFactory",
                hostname, port, database, username, password);
    }
}
