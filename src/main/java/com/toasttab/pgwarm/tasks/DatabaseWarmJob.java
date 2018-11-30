package com.toasttab.pgwarm.tasks;

import com.github.tomaslanger.chalk.Ansi;
import com.toasttab.pgwarm.db.DatabaseRelationshipFinder;
import com.toasttab.pgwarm.db.PrewarmMode;
import com.toasttab.pgwarm.db.Relationship;
import com.toasttab.pgwarm.db.filters.RelationshipFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseWarmJob {
    private final BasicDataSource pool;
    private final List<RelationshipFilter> filters;
    private final PrewarmMode mode;
    private final int numWorkers;

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ConcurrentLinkedQueue<Relationship> workQueue;

    public DatabaseWarmJob(BasicDataSource pool, List<RelationshipFilter> filters, int workers, PrewarmMode mode) {
        this.pool = pool;
        this.filters = filters;
        this.numWorkers = workers;
        this.mode = mode;

        executorService = Executors.newFixedThreadPool(workers);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        workQueue = new ConcurrentLinkedQueue<>();
    }

    public PrewarmMode getPrewarmMode() {
        return mode;
    }

    public final Relationship getNextRelationship() {
        return workQueue.poll();
    }

    public final BasicDataSource getConnectionPool() {
        return this.pool;
    }

    public void run() throws Exception {
        workQueue.addAll(
                new DatabaseRelationshipFinder(pool, filters).getRelationships()
        );
        final int totalRelations = workQueue.size();
        final List<RelationWarmupWorker> workers = new ArrayList<>();
        for(int i = 0; i < numWorkers; i++) {
            RelationWarmupWorker worker = new RelationWarmupWorker(this);
            workers.add(worker);
            executorService.execute(worker);
            System.out.println();
        }
        System.out.println();

        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    for(int i = 0; i < numWorkers + 1; i++) {
                        System.out.print(Ansi.cursorUp(1));
                        System.out.print(Ansi.eraseLine());
                    }
                    System.out.println(String.format("Working on relation %d of %d",
                            totalRelations - workQueue.size(), totalRelations));
                    System.out.println(workers.stream().map(RelationWarmupWorker::getProgressString).collect(
                            Collectors.joining("\n")));

                },
                0, 1, TimeUnit.SECONDS);

        executorService.shutdown();
        executorService.awaitTermination(24, TimeUnit.HOURS);
        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(24, TimeUnit.HOURS);
    }
}
