package model.db;

import java.sql.*;

public class CreateTable {
    private Connection conn = null;

    public CreateTable(Connection conn) {
        this.conn = conn;
    }

    // create table
    public boolean create(String tableName, String createStmt) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables != null) {
                if (tables.next()) {
                    System.out.println("Table " + tableName + " exists.");
                    throw new Exception("Table " + tableName + " exists.");
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    int result = stmt.executeUpdate(createStmt);
                    //"Create table EMPLOYEE(ENO varchar(10),name varchar(250),salary float)"
                    if (result == 0) {
                        System.out.println("Table has been created successfully");
                        return true;
                    } else {
                        System.out.println("Table is not created");
                        // throw new Exception("There is some while creating " + tableName + " table.");
                    }
                }
                tables.close();
            } else {
                System.out.println(("Problem with retrieving database metadata"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        return false;
    }

    // delete table
    public boolean drop(String tableName) {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables != null) {
                if (tables.next()) {
                    System.out.println("Table " + tableName + " exists.");
                    int result = stmt.executeUpdate("drop table "+tableName);
                    if (result == 0) {
                        System.out.println("Table has been deleted successfully");
                        return true;
                    } else {
                        System.out.println("Table is not deleted");
                    }
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    throw new Exception("Table " + tableName + " does not exist.");
                }
                tables.close();
            } else {
                System.out.println("Problem with retrieving database metadata");
                throw new Exception("Problem with retrieving database metadata");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

}
