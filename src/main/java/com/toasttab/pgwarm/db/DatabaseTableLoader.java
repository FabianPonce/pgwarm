package com.toasttab.pgwarm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTableLoader {
    private final static String TABLE_FINDER_SQL =
            "select nsp.nspname as object_schema," +
            "       cls.relname as object_name " +
            "from pg_class cls" +
            "  join pg_roles rol on rol.oid = cls.relowner" +
            "  join pg_namespace nsp on nsp.oid = cls.relnamespace " +
            "where cls.relkind = 'r'";

    private final Connection connection;
    private final RelationshipFactory factory;
    public DatabaseTableLoader(Connection con, RelationshipFactory factory) {
        this.connection = con;
        this.factory = factory;
    }

    public void loadTables() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(TABLE_FINDER_SQL);
        ResultSet result = statement.executeQuery();
        while(result.next()) {
            String tableName = result.getString(2);
            String schema = result.getString(1);
            factory.newTable(schema, tableName);
        }
    }
}
