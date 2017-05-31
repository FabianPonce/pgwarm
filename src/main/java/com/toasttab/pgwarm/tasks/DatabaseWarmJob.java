package com.toasttab.pgwarm.tasks;

import com.toasttab.pgwarm.db.DatabaseRelationship;
import com.toasttab.pgwarm.db.DatabaseRelationshipFinder;
import com.toasttab.pgwarm.db.filters.SchemaRelationshipFilter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class DatabaseWarmJob {
    private final BasicDataSource pool;
    private final List<String> schemas;
    private final int workers;

    private final ConcurrentLinkedQueue<DatabaseRelationship> workQueue = new ConcurrentLinkedQueue<DatabaseRelationship>();

    public DatabaseWarmJob(BasicDataSource pool, List<String> schemas, int workers) {
        this.pool = pool;
        this.schemas = schemas;
        this.workers = workers;
    }

    public final DatabaseRelationship getNextRelationship() {
        return workQueue.poll();
    }

    public final BasicDataSource getConnectionPool() {
        return this.pool;
    }

    public void run() {
        workQueue.addAll(new DatabaseRelationshipFinder(pool, new SchemaRelationshipFilter(this.schemas)).getRelationships());

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
