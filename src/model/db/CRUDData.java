package model.db;

import java.sql.*;

public class CRUDData {

    private Connection conn = null;

    public CRUDData() {

    }

    public CRUDData(Connection conn) {
        this.conn = conn;
    }

    public static boolean isResultSetEmpty(ResultSet resultSet) throws SQLException {
        return !resultSet.first();
    }

    public boolean insert(String tableName, String insertStmt) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables != null) {
                if (tables.next()) {
                    // System.out.println("Table " + tableName + " exists.");
                    int result = stmt.executeUpdate(insertStmt);
                    if (result > 0) {
                        System.out.println(result + " row(s) has been inserted successfully");
                        return true;
                    } else {
                        // System.out.println("Row is not created");
                        //System.out.println(result);
                        throw new Exception("Row is not created");
                    }
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    throw new Exception("Table " + tableName + "does not exists.");
                }
            } else {
                System.out.println(("Problem with retrieving database metadata"));
            }
        } catch (Exception ex) {
            // System.out.println(ex.getMessage());
            throw ex;
        } finally {
            //tables.close();
        }
        return false;
    }

    public ResultSet select(String tableName, String selectStmt) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables != null) {
                if (tables.next()) {
                    // System.out.println("Table " + tableName + " exists.");
                    ResultSet rs = stmt.executeQuery(selectStmt);
                    return rs;
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    throw new Exception("Table " + tableName + " does not exists.");
                }
            } else {
                System.out.println(("Problem with retrieving database metadata"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            // tables.close();
        }
        return null;
    }

    public boolean delete(String tableName, String selectStmt) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables != null) {
                if (tables.next()) {
                    // System.out.println("Table " + tableName + " exists.");
                    int result = stmt.executeUpdate(selectStmt);
                    return true;
                } else {
                    System.out.println("Table " + tableName + " does not exist.");
                    throw new Exception("Table " + tableName + " does not exists.");
                }
            } else {
                System.out.println(("Problem with retrieving database metadata"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            // tables.close();
        }
        return false;
    }
}

