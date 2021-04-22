package model.db;

import java.sql.Connection;
import java.sql.ResultSet;

public class DB {
    private ConnectDB cb = null;
    private CreateTable createTable = null;
    private CRUDData CRUDData = null;
    public DB(){
        cb = new ConnectDB();
        createTable = new CreateTable(cb.getConn());
        CRUDData = new CRUDData(cb.getConn());
        //cb.closeDB();
    }

    public Connection getConnection(){
        return cb.getConn();
    }

    public boolean closeConnection(){
        return cb.closeDB();
    }

    public boolean createTable(String tableName, String statement) throws Exception {
        return createTable.create(tableName, statement);
    }

    public boolean dropTable(String tableName) throws Exception {
        return createTable.drop(tableName);
    }
    public boolean insertData(String tableName, String statement) throws Exception{
        return CRUDData.insert(tableName,statement);
    }

    public ResultSet selectData(String tableName, String statement) throws Exception{
        return CRUDData.select(tableName,statement);
    }

    public boolean deleteData(String tableName, String statement) throws Exception{
        return CRUDData.delete(tableName,statement);
    }
}
