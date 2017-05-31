package com.toasttab.pgwarm.db;

import com.toasttab.pgwarm.db.filters.RelationshipFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRelationshipFinder {
    private final Connection connection;
    private final RelationshipFilter filter;
    public DatabaseRelationshipFinder(Connection con, RelationshipFilter filter) {
        this.connection = con;
        this.filter = filter;
    }

    public List<DatabaseRelationship> getRelationships() {
        ArrayList<DatabaseRelationship> retList = new ArrayList<DatabaseRelationship>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT table_name AS relname, table_schema AS schema, 'r' AS relkind FROM information_schema.tables UNION " +
                    "SELECT relname, schemaname AS schema, 'i' AS relkind FROM pg_stat_all_indexes"
            );
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                DatabaseRelationship rel = new DatabaseRelationship(result.getString("relname"), result.getString("schema"), RelationshipType.fromRelKind(result.getString("relkind")));
                if(this.filter.filter(rel))
                    retList.add(rel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retList;
    }
}
