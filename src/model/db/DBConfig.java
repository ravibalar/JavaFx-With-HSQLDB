package model.db;

public class DBConfig {
    public static String driver = "org.hsqldb.jdbc.JDBCDriver";
    public static String connectionString = "jdbc:hsqldb:file:";
    public static String db = "database/UniLinkDB";
    public static String db_lock = ";hsqldb.lock_file=false";
    public static String username = "SA";
    public static String password = null;
}
