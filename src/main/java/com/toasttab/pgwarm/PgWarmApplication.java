package com.toasttab.pgwarm;

import com.beust.jcommander.JCommander;
import com.toasttab.pgwarm.db.filters.RelationshipFilter;
import com.toasttab.pgwarm.db.filters.RelationshipNameFilter;
import com.toasttab.pgwarm.db.filters.RelationshipSchemaFilter;
import com.toasttab.pgwarm.db.util.DatabaseConnectionStringBuilder;
import com.toasttab.pgwarm.tasks.DatabaseWarmJob;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.ArrayList;
import java.util.List;

public class PgWarmApplication {

    private static final String BINARY_NAME = "pgwarm";

    public static void main(String[] args) throws Exception {
        ApplicationArguments arguments = new ApplicationArguments();

        JCommander commander = JCommander
                .newBuilder().addObject(arguments)
                .build();

        commander.setProgramName(BINARY_NAME);

        commander.parse(args);

        String connectionString = new DatabaseConnectionStringBuilder()
                .withHostname(arguments.dbHost)
                .withPort(arguments.dbPort)
                .withDatabase(arguments.dbName)
                .withUsername(arguments.dbUser)
                .withPassword(arguments.dbPassword)
                .withApplicationName(BINARY_NAME)
                .toString();

        if(arguments.help) {
            commander.usage();
            return;
        }

        System.out.println(String.format("%s, initiating database warming...", BINARY_NAME));

        BasicDataSource connectionPool = new BasicDataSource();
        connectionPool.setUsername(arguments.dbUser);
        connectionPool.setPassword(arguments.dbPassword);
        connectionPool.setDriverClassName("org.postgresql.Driver");
        connectionPool.setInitialSize(arguments.workers+1);
        connectionPool.setUrl(connectionString);

        new DatabaseWarmJob(connectionPool, getFiltersFromArguments(arguments), arguments.workers, arguments.prewarmMode).run();

        System.out.println();
        System.out.println("Warming complete!");
    }

    private static List<RelationshipFilter> getFiltersFromArguments(ApplicationArguments args) {
        ArrayList<RelationshipFilter> filters = new ArrayList<RelationshipFilter>();

        filters.add(new RelationshipSchemaFilter(args.schemas));

        if(args.relations.size() > 0) {
            filters.add(new RelationshipNameFilter(args.relations));
        }

        return filters;
    }
}
