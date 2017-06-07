package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.DatabaseRelationship;

import java.sql.SQLException;

public class RelationWarmupWorker implements Runnable {
    private final DatabaseWarmJob job;

    public RelationWarmupWorker(DatabaseWarmJob job) {
        this.job = job;
    }

    public void run() {
        DatabaseRelationship relationship = null;
        while((relationship = job.getNextRelationship()) != null) {
            try {
                new RelationWarmupTask(job.getConnectionPool().getConnection(), relationship, job.getPrewarmMode()).run();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
