package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.DatabaseRelationship;
import com.toasttab.pgwarm.db.DatabaseRelationshipFinder;
import com.toasttab.pgwarm.db.filters.SchemaRelationshipFilter;

import java.sql.Connection;
import java.util.List;

public class DatabaseWarmJob {
    private final Connection connection;
    private final List<String> schemas;

    public DatabaseWarmJob(Connection con, List<String> schemas) {
        this.connection = con;
        this.schemas = schemas;
    }

    public void run() {
        List<DatabaseRelationship> relationships = new DatabaseRelationshipFinder(connection, new SchemaRelationshipFilter(this.schemas)).getRelationships();

        for(DatabaseRelationship relation : relationships) {
            new RelationWarmupTask(connection, relation).run();
        }
    }
}
