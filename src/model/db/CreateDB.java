package model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDB {
    private Connection conn = null;
    public CreateDB() {
        try {
            Class.forName(DBConfig.driver);
        } catch (Exception ex) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            ex.printStackTrace();
            return;
        }
        try {
            conn = DriverManager.getConnection(DBConfig.connectionString + DBConfig.db, DBConfig.username, DBConfig.password);
            if(conn != null) System.out.println(conn);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}
