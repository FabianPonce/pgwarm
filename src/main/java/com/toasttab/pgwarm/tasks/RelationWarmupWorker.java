package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.Relationship;

import java.sql.SQLException;

public class RelationWarmupWorker implements Runnable {
    private final DatabaseWarmJob job;

    public RelationWarmupWorker(DatabaseWarmJob job) {
        this.job = job;
    }

    public void run() {
        Relationship relationship = null;
        while((relationship = job.getNextRelationship()) != null) {
            try {
                new RelationWarmupTask(job.getConnectionPool().getConnection(), relationship, job.getPrewarmMode()).run();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
