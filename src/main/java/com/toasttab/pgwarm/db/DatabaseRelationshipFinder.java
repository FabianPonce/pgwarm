package com.toasttab.pgwarm.db;

import com.toasttab.pgwarm.db.filters.RelationshipFilter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRelationshipFinder {
    private final BasicDataSource pool;
    private final List<RelationshipFilter> filters;
    public DatabaseRelationshipFinder(BasicDataSource pool, List<RelationshipFilter> filters) {
        this.pool = pool;
        this.filters = filters;
    }

    public List<Relationship> getRelationships() throws SQLException {
        ArrayList<Relationship> retList = new ArrayList<Relationship>();
        Connection connection = pool.getConnection();

        RelationshipFactory factory = new RelationshipFactory();

        new DatabaseTableLoader(connection, factory).loadTables();
        new DatabaseIndexLoader(connection, factory).loadIndices();

        for(Relationship rel : factory.getRelations()) {
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
        connection.close();

        return retList;
    }
}
