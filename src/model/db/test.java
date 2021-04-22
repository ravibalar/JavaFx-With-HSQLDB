package model.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test {

    public static void main(String[] args) {
        ConnectDB cb = null;
        cb = new ConnectDB();
        cb.closeDB();
    }
}