package com.toasttab.pgwarm;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import java.util.ArrayList;
import java.util.List;

public class ApplicationArguments {
    @Parameter(names = { "--host", "-h" }, description = "The hostname or IP address of the target database")
    public String dbHost = "";

    @Parameter(names = { "--port", "-P" }, description = "The port number the target database is listening for connections on")
    public int dbPort = 5432;

    @Parameter(names = { "--database", "-d" }, description = "The database name to connect to")
    public String dbName = "";

    @Parameter(names = { "--username", "-u" }, description = "The database user to connect with")
    public String dbUser = "";

    @Parameter(names = { "--password", "-p" }, description = "The password to connect with")
    public String dbPassword = "";

    @Parameter(names = { "--schema", "-s" }, description = "The schema(s) to warm")
    public List<String> schemas = new ArrayList<String>(Lists.newArrayList(new String[] { "public" }));

    @Parameter(names = { "--workers", "-w"}, description = "The number of parallel relations that can be warmed at any given time")
    public int workers = 4;

    @Parameter(names = { "--help" }, description = "Display the help text", help = true)
    public boolean help;
}
