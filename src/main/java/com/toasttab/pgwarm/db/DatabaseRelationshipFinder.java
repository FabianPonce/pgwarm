package com.toasttab.pgwarm.db;

import com.toasttab.pgwarm.db.filters.RelationshipFilter;
import com.toasttab.pgwarm.db.util.SQLUtility;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatabaseRelationshipFinder {
    private final BasicDataSource pool;
    private final List<RelationshipFilter> filters;
    public DatabaseRelationshipFinder(BasicDataSource pool, List<RelationshipFilter> filters) {
        this.pool = pool;
        this.filters = filters;
    }

    public List<DatabaseRelationship> getRelationships() {
        ArrayList<DatabaseRelationship> retList = new ArrayList<DatabaseRelationship>();
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT table_name AS relname, table_schema AS schema, 'r' AS relkind FROM information_schema.tables UNION " +
                    "SELECT relname, schemaname AS schema, 'i' AS relkind FROM pg_stat_all_indexes"
            );
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                DatabaseRelationship rel = new DatabaseRelationship(result.getString("relname"), result.getString("schema"), RelationshipType.fromRelKind(result.getString("relkind")));
                // Test the relationship against all filters. Any failures, and we reject.
                boolean passes = true;
                for(RelationshipFilter filter : filters) {
                    if(!filter.filter(rel)) {
                        passes = false;
                        break;
                    }
                }

                if(passes)
                    retList.add(rel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null)
                SQLUtility.closeQuietly(connection);
        }

        return retList;
    }
}
