# pgwarm
pgwarm attempts to solve the problem of how to eliminate the first touch penalty on new RDS instances. It calls out to pg_prewarm for selected relations in the database, and paginates access over larger tables so that it is suitable for use on read replicas which ordinarily would timeout.

By default, all of the tables and indexes in the public schema are read from the disk.

## Usage
You can display the usage via the --help parameter.
```
Usage: pgwarm [options]
  Options:
    --database, -d
      The database name to connect to
      Default: <empty string>
    --help
      Display the help text
    --host, -h
      The hostname or IP address of the target database
      Default: <empty string>
    --password, -p
      The password to connect with
      Default: <empty string>
    --port, -P
      The port number the target database is listening for connections on
      Default: 5432
    --schema, -s
      The schema(s) to warm
      Default: [public]
    --username, -u
      The database user to connect with
      Default: <empty string>
```

## Example
```
$# ./pgwarm -h preproduction-toastorders-2.cuscngx50y2y.us-east-1.rds.amazonaws.com -u toastapp_g1 -d toastorders
pgwarm, initiating database warming...

public."AccountingReportConfig" [================================================================================] (100%)
public."MenuItem_Portion" [================================================================================] (100%)
public."ToastCard" [================================================================================] (100%)
public."Orders" [====================                                                            ] (25%)
```

