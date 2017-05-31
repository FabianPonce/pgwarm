package com.toasttab.pgwarm;

import com.beust.jcommander.JCommander;
import com.toasttab.pgwarm.db.util.DatabaseConnectionStringBuilder;
import com.toasttab.pgwarm.tasks.DatabaseWarmJob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgWarmApplication {

    private static final String BINARY_NAME = "pgwarm";

    public static void main(String[] args) {
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
                .toString();

        if(arguments.help) {
            commander.usage();
            return;
        }

        System.out.println(String.format("%s, initiating database warming...", BINARY_NAME));

        try {
            Connection con = DriverManager.getConnection(connectionString);
            new DatabaseWarmJob(con, arguments.schemas).run();
        } catch (SQLException e) {
            System.err.println(e.toString());
        }

        System.out.println();
        System.out.println("Warming complete!");
    }
}
