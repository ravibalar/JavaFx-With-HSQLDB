package service;

import model.db.DB;

import java.sql.ResultSet;

public class DBService {
    private final DB db = new DB();

    // Create post table
    public void createPost() throws Exception {
        System.out.println("Create Post table");
        String tableName = "POST";
        String tableStatement = "Create table POST(" +
                "postType varchar(10)," +
                "ID  varchar(10)," +
                "title  varchar(100)," +
                "description varchar(250)," +
                "imageName varchar(100)," +
                "creatorID varchar(10)," +
                "status varchar(10)," +
                "venue varchar(100)," +
                "date varchar(100)," +
                "capacity int," +
                "askingPrice float," +
                "minimumRaise float," +
                "proposedPrice float," +
                "replies varchar(255)," +
                " Primary Key (ID)" +
                ")";
        db.dropTable(tableName);
        if (db.createTable(tableName, tableStatement)) {
            System.out.println("Table has been created");
        }
    }

    // Close connection
    public boolean closeConnection() {
        return db.closeConnection();
    }

    // Add new post(s) to db
    public boolean addPost(String tableName, String row) throws Exception {
        //String stmt = "insert into Post values('EVENT','EVE001','EVE001','EVE001 description','default.png','S3','OPEN','Cinema','12/04/2020',4,0.0,0.0,0.0,'')";
        try {
            db.insertData(tableName, row);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    // delete post with where clause
    public boolean deletePost(String tableName, String where) throws Exception {
        //String stmt = "insert into Post values('EVENT','EVE001','EVE001','EVE001 description','default.png','S3','OPEN','Cinema','12/04/2020',4,0.0,0.0,0.0,'')";
        try {
            db.deleteData(tableName, "delete from " + tableName + (where != "" ? "where " + where : ""));
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    // Select data from table
    public ResultSet select(String tableName, String stmt) throws Exception {
        return db.selectData(tableName, stmt);
    }
}
