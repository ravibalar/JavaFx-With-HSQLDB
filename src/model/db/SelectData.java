package model.db;

import java.sql.*;

public class SelectData {

    private Connection conn = null;

    public SelectData(){

    }
    public SelectData(Connection conn){
        this.conn = conn;
    }

    public ResultSet select(String tableName,String selectStmt) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables != null) {
                if (tables.next()) {
                    // System.out.println("Table " + tableName + " exists.");
                    ResultSet rs = stmt.executeQuery(selectStmt);
                    if(isResultSetEmpty(rs)){
                        System.out.println("No data found");
                        throw new Exception("No data found");
                    }else{
                        System.out.println("Data has been selected successfully");
                        //rs.beforeFirst();
                        return rs;
                    }
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    throw new Exception("Table " + tableName + "does not exists.");
                }
            } else {
                System.out.println(("Problem with retrieving database metadata"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }finally {
            // tables.close();
        }
        return null;
    }

    public static boolean isResultSetEmpty(ResultSet resultSet) throws SQLException {
        return !resultSet.first();
    }
}

