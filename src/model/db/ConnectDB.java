package model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private Connection conn = null;

    public ConnectDB() {
        try {
            Class.forName(DBConfig.driver);
            conn = DriverManager.getConnection(DBConfig.connectionString + DBConfig.db + DBConfig.db_lock, DBConfig.username, DBConfig.password);
            if (conn != null) System.out.println(conn);
        } catch (SQLException throwables) {
            System.out.println("Error");
            throwables.printStackTrace();
        } catch (Exception ex) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            ex.printStackTrace();
            return;
        }
    }

    public Connection getConn() {
        return conn;
    }

    public boolean closeDB() {
        try {
            if (conn != null)
                conn.close();
            return true;
        } catch (SQLException throwables) {
            System.out.println("Error While Closing db");
            throwables.printStackTrace();
            return false;
        }
    }
}
