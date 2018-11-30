package com.toasttab.pgwarm.db.util;

public class DatabaseConnectionStringBuilder {
    private String hostname = "";
    private String username = "";
    private String password = "";
    private int port        = 5432;
    private boolean ssl = true;
    private String database = "";
    private String applicationName = "";

    public DatabaseConnectionStringBuilder() {
    }

    public final DatabaseConnectionStringBuilder withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public final DatabaseConnectionStringBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public final DatabaseConnectionStringBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public final DatabaseConnectionStringBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public final DatabaseConnectionStringBuilder withSsl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public final DatabaseConnectionStringBuilder withDatabase(String database) {
        this.database = database;
        return this;
    }

    public final DatabaseConnectionStringBuilder withApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public final String toString() {
        String connectionString = String.format("jdbc:postgresql://%s:%s/%s?ssl=%s&user=%s&password=%s%s&ApplicationName=%s",
                hostname, port, database, ssl ? "true":"disable", username, password,
                ssl ? "&sslfactory=org.postgresql.ssl.NonValidatingFactory":"", applicationName);
        //System.out.println(connectionString);
        return connectionString;
    }
}
