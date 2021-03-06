package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.PrewarmMode;
import com.toasttab.pgwarm.db.Relationship;
import com.toasttab.pgwarm.db.util.PSQLUtility;
import com.toasttab.pgwarm.util.ConsoleProgressBar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationWarmupTask {
    /* This setting controls the maximum number of PostgreSQL blocks to prewarm in one query.
     * By default, PostgreSQL contains 8,196 bytes per block. Therefore the default of 1,000 represents a read of
     * 8.196 MB of data.
     */
    private static final int MAX_BLOCK_READ_SIZE = 1000;

    /**
     * The base interval for retries that should occur, in milliseconds. We use exponential backoff in order to protect
     * the database we're querying against too much load. In the event a query fails, the formula for how long to wait
     * will be (RETRY_INTERVAL)*10^(retry_count-1).
     */
    private static final int RETRY_INTERVAL = 100;

    // The maximum number of times we will retry running a pg_prewarm operation before failing this task.
    private static final int MAX_RETRY_COUNT = 5;

    private final Connection connection;
    private final Relationship relation;
    private final PrewarmMode mode;

    private volatile int maxBlockId;
    private volatile int currBlock;

    public RelationWarmupTask(Connection conn, Relationship relation, PrewarmMode mode) {
        this.connection = conn;
        this.relation = relation;
        this.mode = mode;
        this.maxBlockId = 1;
        this.currBlock = 0;
    }

    private int getTotalBlocks() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(String.format(
                "SELECT pg_relation_size('%s.\"%s\"') / (select setting::int from pg_settings where name = 'block_size' limit 1)",
                relation.getSchema(), relation.getName()
        ));
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }

        throw new SQLException("No results returned.");
    }

    public void run() throws SQLException, InterruptedException {
        maxBlockId = Math.max(0, getTotalBlocks() - 1);
        currBlock = 0;
        int retryCount = 0;

        while(currBlock < maxBlockId) {
            int toBlock = Math.min(currBlock + MAX_BLOCK_READ_SIZE, maxBlockId);

            try {
                PreparedStatement stmt = connection.prepareStatement(String.format(
                        "SELECT pg_prewarm('%s.\"%s\"', ?, 'main', ?, ?)", relation.getSchema(), relation.getName()
                ));
                stmt.setString(1, mode.toSqlArgument());
                stmt.setInt(2, currBlock);
                stmt.setInt(3, toBlock);
                stmt.executeQuery();
                currBlock = toBlock + 1;
                retryCount = 0;
            } catch (SQLException e) {
                if(PSQLUtility.isRetriableSqlState(e.getSQLState()) && retryCount < MAX_RETRY_COUNT) {
                    Thread.sleep((long)(RETRY_INTERVAL * Math.pow(10, retryCount)));
                    retryCount++;
                    continue;
                } else {
                    throw e; //rethrow exception if it's not recoverable.
                }
            }
        }

        currBlock = maxBlockId;

        connection.close();
    }

    public String getProgressString() {
        return String.format("%s.%-40s ", relation.getSchema(), relation.getName()) +
                new ConsoleProgressBar(currBlock, maxBlockId);
    }
}
