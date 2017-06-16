package com.toasttab.pgwarm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseIndexLoader {
    private final static String INDEX_FINDER_SQL =
            "select nsp.nspname as object_schema," +
            "       cls.relname as object_name," +
            "       tcls.relname AS table_name " +
            "from pg_class cls" +
            "  join pg_roles rol on rol.oid = cls.relowner" +
            "  join pg_namespace nsp on nsp.oid = cls.relnamespace" +
            "  join pg_index idx on idx.indexrelid = cls.oid" +
            "  join pg_class tcls on idx.indrelid = tcls.oid " +
            "where cls.relkind = 'i' and tcls.relkind = 'r'";

    private final Connection connection;
    private final RelationshipFactory factory;
    public DatabaseIndexLoader(Connection con, RelationshipFactory factory) {
        this.connection = con;
        this.factory = factory;
    }

    public void loadIndices() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INDEX_FINDER_SQL);
        ResultSet result = statement.executeQuery();
        while(result.next()) {
            String indexName = result.getString(2);
            String schema = result.getString(1);
            String tableName = result.getString(3);

            factory.newIndex(schema, indexName, tableName);
        }
    }
}
