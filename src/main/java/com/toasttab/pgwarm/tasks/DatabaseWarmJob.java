package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.DatabaseRelationship;
import com.toasttab.pgwarm.db.DatabaseRelationshipFinder;
import com.toasttab.pgwarm.db.PrewarmMode;
import com.toasttab.pgwarm.db.filters.RelationshipFilter;
import com.toasttab.pgwarm.db.filters.RelationshipSchemaFilter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class DatabaseWarmJob {
    private final BasicDataSource pool;
    private final List<RelationshipFilter> filters;
    private final int workers;
    private final PrewarmMode mode;

    private final ConcurrentLinkedQueue<DatabaseRelationship> workQueue = new ConcurrentLinkedQueue<DatabaseRelationship>();

    public DatabaseWarmJob(BasicDataSource pool, List<RelationshipFilter> filters, int workers, PrewarmMode mode) {
        this.pool = pool;
        this.filters = filters;
        this.workers = workers;
        this.mode = mode;
    }

    public PrewarmMode getPrewarmMode() {
        return mode;
    }

    public final DatabaseRelationship getNextRelationship() {
        return workQueue.poll();
    }

    public final BasicDataSource getConnectionPool() {
        return this.pool;
    }

    public void run() {
        workQueue.addAll(
                new DatabaseRelationshipFinder(pool, filters).getRelationships()
        );

        List<Thread> threads = new ArrayList<Thread>();
        for(int i = 0; i < workers; i++) {
            RelationWarmupWorker worker = new RelationWarmupWorker(this);
            Thread t = new Thread(worker);
            t.setName(String.format("DatabaseWarmUpWorker - %d", i));
            t.start();

            threads.add(t);
        }

        threads.forEach(new Consumer<Thread>() {
            public void accept(Thread thread) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
