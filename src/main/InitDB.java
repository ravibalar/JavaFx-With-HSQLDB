package main;

import model.user.User;
import service.DBService;
import service.UniLinkService;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InitDB {

    public static void main(String[] args) {
        UniLinkService uniLinkService = UniLinkService.getInstance();
        DBService dbService = new DBService();
        final String TABLE_NAME = "POST";
        try {
            User user = new User("balar025");
            String tableName = TABLE_NAME.toUpperCase();
            dbService.createPost();
            int row = 0;
            try {
                File file = new File("src/data/default_data.txt");
                uniLinkService.importData(user, file);
                uniLinkService.saveData();
                // dbService.addPost(tableName, "insert into Post values " +
                // "('EVENT','EVE001','EVE001','EVE001
                // description','default.png','S3','OPEN','Cinema','12/04/2020',4,0.0,0.0,0.0,''),"
                // +
                // "('EVENT','EVE002','EVE002','EVE002
                // description','default.png','S3','OPEN','Cinema2','12/04/2020',4,0.0,0.0,0.0,'')");
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

            ResultSet rs = dbService.select("Post", "select * from post");
            int noOfColumns = rs.getMetaData().getColumnCount();
            String[] dataStr;// = new String[noOfColumns];
            while (rs.next()) {
                List rowValues = new ArrayList();
                for (int i = 1; i <= noOfColumns; i++) {
                    // System.out.println(rs.getMetaData().getColumnName(i));
                    rowValues.add(rs.getString(i));
                }
                System.out.println("Replies:" + rs.getString("replies"));
                System.out.println(rowValues.size());
                dataStr = (String[]) rowValues.toArray(new String[rowValues.size()]);
                System.out.println(dataStr);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            // dbService.closeConnection();
        }
    }
}
