package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.DatabaseRelationship;
import com.toasttab.pgwarm.util.ConsoleProgressBar;

import java.sql.*;

public class RelationWarmupTask {
    /* This setting controls the maximum number of PostgreSQL blocks to prewarm in one query.
     * By default, PostgreSQL contains 8,196 bytes per block. Therefore the default of 100,000 represents a read of
     * 819.6 MB of data.
     */
    private static final int MAX_BLOCK_READ_SIZE = 100000;

    private Connection connection;
    private DatabaseRelationship relation;

    public RelationWarmupTask(Connection conn, DatabaseRelationship relation) {
        this.connection = conn;
        this.relation = relation;
    }

    private int getTotalBlocks() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(String.format(
                "SELECT pg_relation_size('%s.\"%s\"') / (select setting::int from pg_settings where name = 'block_size' limit 1)",
                relation.getSchema(), relation.getName()
        ));
        ResultSet result = stmt.executeQuery();
        while(result.next()) {
            return result.getInt(1);
        }

        throw new SQLException("No results returned.");
    }

    public void run() {
        System.out.println();

        try {
            int maxBlockId = Math.max(0, getTotalBlocks() - 1);
            int currBlock = 0;

            while(currBlock < maxBlockId) {
                int toBlock = Math.min(currBlock + MAX_BLOCK_READ_SIZE, maxBlockId);

                printProgress( (int) ((float)currBlock / (float)maxBlockId * 100) );

                PreparedStatement stmt = connection.prepareStatement(String.format(
                        "SELECT pg_prewarm('%s.\"%s\"', 'read', 'main', ?, ?)", relation.getSchema(), relation.getName()
                ));
                stmt.setInt(1, currBlock);
                stmt.setInt(2, toBlock);
                stmt.executeQuery();

                currBlock = toBlock + 1;
            }

            printProgress(100);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void printProgress(int percent) {
        System.out.print("\r");
        System.out.print(String.format("%s.\"%s\" ", relation.getSchema(), relation.getName()));
        System.out.print(new ConsoleProgressBar(percent));
    }
}
