package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.Relationship;
import java.sql.SQLException;

public class RelationWarmupWorker implements Runnable {
    private final DatabaseWarmJob job;
    private volatile RelationWarmupTask currentTask;

    public RelationWarmupWorker(DatabaseWarmJob job) {
        this.job = job;
    }

    @Override
    public void run() {
        Relationship relationship = null;
        while((relationship = job.getNextRelationship()) != null) {
            try {
                RelationWarmupTask task = new RelationWarmupTask(
                        job.getConnectionPool().getConnection(), relationship, job.getPrewarmMode());
                currentTask = task;
                task.run();
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProgressString() {
        return currentTask == null ? "" : currentTask.getProgressString();
    }
}
