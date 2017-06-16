package com.toasttab.pgwarm;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import com.toasttab.pgwarm.db.PrewarmMode;

import java.util.ArrayList;
import java.util.List;

public class ApplicationArguments {
    public static final String PASSWORD_ENV_VARIABLE = "PGPASSWORD";

    @Parameter(names = { "--host", "-h" }, description = "The hostname or IP address of the target database")
    public String dbHost = "";

    @Parameter(names = { "--port", "-P" }, description = "The port number the target database is listening for connections on")
    public int dbPort = 5432;

    @Parameter(names = { "--database", "-d" }, description = "The database name to connect to")
    public String dbName = "";

    @Parameter(names = { "--username", "-u" }, description = "The database user to connect with")
    public String dbUser = "";

    @Parameter(names = { "--password", "-p" },
            description = "The password to connect with. You may also set the environment variable "
                    + PASSWORD_ENV_VARIABLE + " in order to prevent the password from being exposed in the process" +
                    " list.")
    public String dbPassword = "";

    @Parameter(names = { "--schema", "-s" }, description = "The schema(s) to warm")
    public List<String> schemas = new ArrayList<String>(Lists.newArrayList(new String[] { "public" }));

    @Parameter(names = { "--relations", "-r" }, description = "A list of relations to warm")
    public List<String> relations = new ArrayList<String>();

    @Parameter(names = { "--workers", "-w"}, description = "The number of parallel relations that can be warmed at any given time")
    public int workers = 1;

    @Parameter(names = { "--mode", "-m" }, description = "The mode to pass to pg_prewarm", converter = PrewarmModeConverter.class)
    public PrewarmMode prewarmMode = PrewarmMode.READ;

    @Parameter(names = { "--help" }, description = "Display the help text", help = true)
    public boolean help;
}
