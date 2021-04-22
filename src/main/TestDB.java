package main;

import model.db.DB;

public class TestDB {

    public static void main(String[] args) {
//        UniLinkService uniLinkService = UniLinkService.getInstance();
//        FileCSVOperation fileCSVOperation = new FileCSVOperation("POST_TABLE");
//        List<String> data = fileCSVOperation.readCSV();
//        System.out.println(data);
//        for (int i = 1; i < data.size() && data.get(0) != ""; i++) {
//            System.out.println(data.get(i));
//            String[] dataStr = data.get(i).split("\\|");
//            System.out.println(dataStr[0]);
//            String id = "TEMP";
//            StringBuilder objectStr = new StringBuilder();
//            objectStr.append(id + ",");
//            switch (dataStr[0].toUpperCase()) {
//                case "EVENT":
//                    System.out.println("Event object");
//                    Event tempEvent = new Event();
//                    tempEvent.setID(id);
//                    tempEvent.setTitle(dataStr[1]);
//                    tempEvent.setDescription(dataStr[2]);
//                    tempEvent.setImageName(dataStr[3]);
//                    tempEvent.setCreatorID("S1");
//                    tempEvent.setVenue(dataStr[4]);
//                    tempEvent.setDate(dataStr[5]);
//                    tempEvent.setCapacity(Integer.parseInt(dataStr[6]));
//                    try {
//                        Post post = uniLinkService.newEvent(tempEvent);
//                        System.out.println("Post is created:" + post.getID());
//                    } catch (Exception ex) {
//                        System.out.println(ex.toString());
//                    }
//                    break;
//                case "SALE":
//                    System.out.println("Sale object");
//                    Sale tempSale = new Sale();
//                    tempSale.setID(id);
//                    tempSale.setTitle(dataStr[1]);
//                    tempSale.setDescription(dataStr[2]);
//                    tempSale.setImageName(dataStr[3]);
//                    tempSale.setCreatorID("S1");
//                    tempSale.setAskingPrice(Double.parseDouble(dataStr[7]));
//                    tempSale.setMinimumRaise(Double.parseDouble(dataStr[8]));
//                    try {
//                        Post post = uniLinkService.newSale(tempSale);
//                        System.out.println("Post is created:" + post.getID());
//                    } catch (Exception ex) {
//                        System.out.println(ex.toString());
//                    }
//                    break;
//                case "JOB":
//                    System.out.println("Job object");
//                    Job tempJob = new Job();
//                    tempJob.setID(id);
//                    tempJob.setTitle(dataStr[1]);
//                    tempJob.setDescription(dataStr[2]);
//                    tempJob.setImageName(dataStr[3]);
//                    tempJob.setCreatorID("S1");
//                    tempJob.setProposedPrice(Double.parseDouble(dataStr[9]));
//                    try {
//                        Post post = uniLinkService.newJob(tempJob);
//                        System.out.println("Post is created:" + post.getID());
//                    } catch (Exception ex) {
//                        System.out.println(ex.toString());
//                    }
//                    break;
//                default:
//                    System.out.println("Invalid string");
//                    System.out.println(dataStr.toString());
//                    break;
//            }
//        }
//        System.out.println(uniLinkService.getAllPost().size());
        DB dbService = new DB();
        final String TABLE_NAME = "POST";
        try {
            String tableName = TABLE_NAME.toUpperCase();
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
            dbService.deleteTable(tableName);
            dbService.createTable(tableName, tableStatement);
            int i = 0;
            try {
                dbService.insertData("Post", "insert into Post values " +
                        "('EVENT','EVE001','EVE001','EVE001 description','default.png','S3','OPEN','Cinema','12/04/2020',4,0.0,0.0,0.0,'')," +
                        "('EVENT','EVE002','EVE002','EVE002 description','default.png','S3','OPEN','Cinema2','12/04/2020',4,0.0,0.0,0.0,'')");
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
            dbService.selectData("Post", "select * from post");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            dbService.closeConnection();
        }
    }
}
