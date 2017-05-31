package com.toasttab.pgwarm.db.util;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUtility {
    public static void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
