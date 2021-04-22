package service;

import controller.utility.AlertController;
import model.post.*;
import model.user.User;
import service.post.EventService;
import service.post.JobService;
import service.post.SaleService;
import service.user.UserService;
import utility.*;

import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniLinkService {

    private static UniLinkService uniLinkService = null;
    private final Map<String, Post> posts = new HashMap<String, Post>(); // post list
    private final EventService eventService = new EventService(); // event related service
    private final JobService jobService = new JobService(); // job related service
    private final SaleService saleService = new SaleService(); // sale related service
    private final UserService userService = new UserService(); // user related service
    private final DBService dbService = new DBService();
    private final FileCSVOperation fileCSVOperation = new FileCSVOperation("UNILINK");
    private final String fieldStr = "postType,ID,title,description,imageName,creatorID,status,venue,capacity,askingPrice,minimumRaise,proposedPrice,replies";
    private User currentUser = new User(); // Current User object

    //
    public UniLinkService() {
        try {
            // try {
            // dbService.createPost();
            // } catch (Exception ex) {
            // System.out.println(ex.toString());
            // }
            this.initialize();
        } catch (Exception ex) {
            System.out.println(ex.toString());// e.printStackTrace();
        }
    }

    public static UniLinkService getInstance() {
        if (uniLinkService == null) {
            uniLinkService = new UniLinkService();
        }
        return uniLinkService;
    }

    // Initialize the posts
    public void initialize() throws Exception {
        // --------------------------------
        try {
            // defaultImport();
            // Import data from database or file
            ResultSet rs = dbService.select("Post", "select * from post");
            importData(rs);
            // Default file import
            System.out.println("Initial Posts size: " + posts.size());

        } catch (Exception ex) {
            // ex.printStackTrace();
            System.out.println("UniLink Initialize:" + ex.toString());
        }
        // --------------------------------
    }

    // Default file import
    public void defaultImport() throws CustomException {
        String currentUserID = "balar025";
        userService.userLogin(currentUserID);
        currentUser = userService.getUser();
        File file = new File("src/data/import_data.txt");
        importData(currentUser, file);
        saveData();
    }

    public boolean userLogin(String username) {
        return userService.userLogin(username);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Create New Event
    public Event newEvent(Event newEvent) throws CustomException {
        try {
            newEvent = eventService.post(newEvent); // Create event id
            if (newEvent != null) {
                posts.put(newEvent.getID(), newEvent); // Update all post list
                System.out.println("Success! Event has been successfully created with id " + newEvent.getID());
            } else {
                System.out.println("There is issue while creating event post");
                throw new CustomException("There is issue while creating event post");
            }
            return newEvent;
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
    }

    // Create new Sale
    public Sale newSale(Sale newSale) throws CustomException {
        try {
            if (newSale.getMinimumRaise() >= newSale.getAskingPrice()) {
                System.out.println("Minimum raise needs to be less than asking price");
                throw new CustomException("Minimum raise needs to be less than asking price");
            }
            newSale = saleService.post(newSale); // Create sale id
            if (newSale != null) {
                posts.put(newSale.getID(), newSale); // Update all post list
                System.out.println("Success! Your new sale has been created with id " + newSale.getID());
            } else {
                System.out.println("There is issue while creating sale post");
                throw new CustomException("There is issue while creating sale post");
            }
            return newSale;
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
    }

    // Create new Job
    public Job newJob(Job newJob) throws CustomException {
        try {
            newJob = jobService.post(newJob); // Create job id
            if (newJob != null) {
                posts.put(newJob.getID(), newJob); // Update all post list
                System.out.println("Success! Your job has been created with id " + newJob.getID());
            } else {
                System.out.println("There is issue while creating job post");
                throw new CustomException("There is issue while creating job post");
            }
            return newJob;
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
    }

    // Update existing post
    public boolean updatePost(Post updatedPost) throws CustomException {
        if (posts.get(updatedPost.getID()) != null) {
            posts.put(updatedPost.getID(), updatedPost);
            return true;
        } else {
            System.out.println("There is not post to update");
            throw new CustomException("There is not post to update");
        }
    }

    // Add Reply to post
    public boolean addReply(User user, Reply tempReply) throws Exception {
        try {
            String replyPostID = tempReply.getID().toUpperCase(); // Convert to uppercase
            Post tempPost = posts.get(replyPostID); // Search for postid in post list
            // Check if post is not closed
            if (tempPost != null && tempPost.getStatus() != Status.CLOSED) { // Check post exist or closed?
                // If responder is creator then ignore the reply
                if (tempPost.getCreatorID().equalsIgnoreCase(user.getID())) {
                    System.out.println("You are creator of post!");
                    throw new InvalidAccessException("You are creator of post:" + replyPostID);
                }
                // Reply tempReply = new Reply(tempPost.getID(), replyValue, user.getID());
                if (tempPost.handleReply(tempReply)) { // handle reply
                    posts.replace(tempPost.getID(), tempPost); // replace existing post
                    System.out.println("Offer accepted for post " + tempPost.getID());
                    return true;
                }
            } else {
                if (tempPost == null) { // Check for null/closed
                    throw new NotFoundException("No Post found:" + replyPostID);
                } else if (tempPost.getStatus() == Status.CLOSED) {
                    throw new PostClosedException("Post is closed");
                }
            }
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return false; // Default return false
    }

    // Display posts by postid
    public Post getPostById(String postId) throws CustomException {
        Post tempPost = null;
        try {
            if (posts.size() == 0) { // Check post list size
                System.out.println("No Post found");
                throw new CustomException("No post found");
            } else {
                tempPost = posts.get(postId.toUpperCase());
            }
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return tempPost;
    }

    // Display current logged in user posts
    public Map<String, Post> getPostByUser(User user) {
        Map<String, Post> filterPostList = new HashMap<String, Post>();
        try {
            if (posts.size() == 0) { // Check post list size
                System.out.println("No Post found");
            } else {
                int noOfMyPost = 0;
                for (Map.Entry<String, Post> postSet : posts.entrySet()) {// Iterate through post list
                    Post tempPost = postSet.getValue();
                    // Display with replies if current user is creator of post
                    if (tempPost.getCreatorID().equalsIgnoreCase(user.getID())) {
                        filterPostList.put(tempPost.getID(), tempPost);
                    }
                }
                if (filterPostList.size() == 0) {// Check if noOfMyPost <= 0
                    System.out.println("No post found");
                    // throw new CustomException("No post found for " + user.getID());
                } else {
                    System.out.println("Post found-" + filterPostList.size());
                }
            }
            // System.out.println("***********My Post*****************");
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return filterPostList;
    }

    // Display all posts
    public Map<String, Post> getAllPost() {
        try {
            if (posts.size() == 0) { // Check post list size
                System.out.println("No Post found");
            } else {
                System.out.println("Post found-" + posts.size());
                return posts;
            }
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return null;
    }

    // Close post
    public boolean closePost(User user, String closeID) throws Exception {
        try {// PostID input for closing post
             // String closeID = userInput.inputString("Enter PostID");
            if (closeID != null && closeID.length() > 0) {
                closeID = closeID.toUpperCase(); // Convert to uppercase
                Post tempPost = posts.get(closeID);
                // Check if currentuser is creator of post
                if (tempPost != null && tempPost.getCreatorID().equalsIgnoreCase(user.getID())) {
                    // Check if post is not closed
                    if (tempPost.getStatus() != Status.CLOSED) {
                        tempPost.setStatus(Status.CLOSED);
                        posts.replace(tempPost.getID(), tempPost); // replace existing post with updated
                        System.out.println("Post has been closed");
                        return true;
                    } else {
                        System.out.println("Post is already closed");
                        throw new CustomException("Post is already closed");
                    }
                } else {
                    if (tempPost == null) {
                        throw new NotFoundException("No Post found:" + closeID);
                    } else if (!tempPost.getCreatorID().equalsIgnoreCase(user.getID())) {
                        throw new InvalidAccessException("You are not creator of post:" + closeID);
                    }
                }
            }
        }
        // Post not found & Current user is not creator of post
        catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return false;
    }

    // Delete selected post
    public boolean deletePost(User user, String deleteID) throws Exception {
        try {
            // PostID input for deleting post
            if (deleteID != null && deleteID.length() > 0) {
                deleteID = deleteID.toUpperCase(); // Convert to uppercase
                Post tempPost = posts.get(deleteID);
                // Check if currentuser is creator
                if (tempPost != null && tempPost.getCreatorID().equalsIgnoreCase(user.getID())) {
                    // eventService.delete((Event) tempPost, currentUser);
                    posts.remove(tempPost.getID());
                    System.out.println("Post has been deleted:" + tempPost.getID());
                    return true;
                } else {
                    if (tempPost == null) {
                        throw new NotFoundException("No Post found:" + deleteID);
                    } else if (!tempPost.getCreatorID().equalsIgnoreCase(user.getID())) {
                        throw new InvalidAccessException("You are not creator of post:" + deleteID);
                    }
                }
            } else {
                throw new CustomException("There is some issue while deleting post");
            }
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return false;
    }

    // Update post image
    public boolean updateImage(String postId, String imageName) throws Exception {
        try {
            // Get post object by id
            Post tempPost = posts.get(postId); // Search for postid in post list
            // Check if post is not closed
            if (tempPost != null && tempPost.getStatus() != Status.CLOSED) {
                tempPost.setImageName(imageName);
                posts.replace(tempPost.getID(), tempPost); // replace existing post
                return true;
            } else {
                if (tempPost == null) {
                    throw new NotFoundException("No Post found:" + postId);
                } else if (tempPost.getStatus() == Status.CLOSED) {
                    throw new PostClosedException("Post is closed");
                }
            }
        } catch (Exception ex) { // Default exception throw
            throw ex;
        }
        return false; // Default return false
    }

    // Upload image from source to image folder
    public boolean uploadImage(String source, String destination) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        while (bufferedInputStream.available() > 0) { // read byte from file
            bufferedOutputStream.write(bufferedInputStream.read());
        }
        bufferedInputStream.close();
        bufferedOutputStream.close();
        return true;
    }

    // import data from database
    public void importData(ResultSet rs) {
        int row = 0;
        try {
            int noOfColumns = rs.getMetaData().getColumnCount(); // No of columns
            String[] dataStr;// = new String[noOfColumns];
            while (rs.next()) { // iterate result row
                List rowValues = new ArrayList(); // Create array from result row
                for (int i = 1; i <= noOfColumns; i++)
                    rowValues.add(rs.getString(i));
                dataStr = (String[]) rowValues.toArray(new String[rowValues.size()]);
                // System.out.println(dataStr);
                switch (dataStr[0].toUpperCase()) {// Check post type
                case "EVENT": // type = EVENT
                    System.out.println("Event object import");
                    try {
                        Event newEventObj = eventService.postParse(dataStr); // Parse object
                        if (newEventObj != null) {
                            newEventObj = this.newEvent(newEventObj);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                case "SALE": // type = SALE
                    System.out.println("Sale object import");
                    try {
                        Sale newSaleObj = saleService.postParse(dataStr); // Parse object
                        if (newSaleObj != null) {
                            newSaleObj = this.newSale(newSaleObj);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                case "JOB": // type = JOB
                    System.out.println("Job object import");
                    try {
                        Job newJobObj = jobService.postParse(dataStr); // Parse object
                        if (newJobObj != null) {
                            newJobObj = this.newJob(newJobObj);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                    break;
                default:
                    System.out.println("Invalid string");
                    System.out.println(dataStr.toString());
                    break;
                }
                row++;
            }
            if (row == 0)
                AlertController.Error("Error", null, "No data found");
        } catch (Exception ex) {
            System.out.println("there is some issue while importing data from database");
        }
    }

    // Import data using file
    public void importData(User user, File file) {
        List<String> data = fileCSVOperation.readCSV(file); // readtxt/csv file
        System.out.println("Import init data size:" + data.size());
        int importSize = data.size() - 1;
        for (int i = 1; i < data.size() && data.get(0) != ""; i++) {
            // System.out.println(data.get(i));
            String[] dataStr = data.get(i).split(",");
            dataStr[1] = "TEMP";
            switch (dataStr[0].toUpperCase()) { // Check post type
            case "EVENT": // type = EVENT
                System.out.println("Event object import");
                try {
                    Event newEventObj = eventService.postParse(dataStr); // Parse Object
                    if (newEventObj != null) {
                        newEventObj.setCreatorID(user.getID()); // Set user id of importer
                        newEventObj = this.newEvent(newEventObj);
                        newEventObj.setReplies(eventService.resetReply(newEventObj));
                        posts.replace(newEventObj.getID(), newEventObj);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    importSize--;
                }
                break;
            case "SALE": // type = SALE
                System.out.println("Sale object import");
                try {
                    Sale newSaleObj = saleService.postParse(dataStr); // Parse Object
                    if (newSaleObj != null) {
                        newSaleObj.setCreatorID(user.getID()); // Set user id of importer
                        newSaleObj = this.newSale(newSaleObj);
                        newSaleObj.setReplies(saleService.resetReply(newSaleObj));
                        posts.replace(newSaleObj.getID(), newSaleObj);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    importSize--;
                }
                break;
            case "JOB": // type = JOB
                System.out.println("Job object import");
                try {
                    Job newJobObj = jobService.postParse(dataStr); // Parse Object
                    if (newJobObj != null) {
                        newJobObj.setCreatorID(user.getID()); // Set user id of importer
                        newJobObj = this.newJob(newJobObj);
                        newJobObj.setReplies(jobService.resetReply(newJobObj));
                        posts.replace(newJobObj.getID(), newJobObj);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    importSize--;
                }
                break;
            default:
                System.out.println("Invalid string:" + dataStr.toString());
                importSize--;
                break;
            }
        }
        System.out.println(importSize + " post(s) has been imported");
    }

    // export data into txt file
    public String exportData() throws CustomException {
        String export = String.format("%s", fieldStr.toUpperCase()); // field list
        try {
            for (Map.Entry<String, Post> postSet : posts.entrySet()) { // iterate to all post
                Post tempPost = postSet.getValue();
                // System.out.println(tempPost.toString());
                PostExport tempExport = new PostExport(tempPost); // create export object
                // System.out.println(tempExport.toString());
                export = String.format("%s\n%s", export, tempExport.toString());
            }
        } catch (Exception ex) {
            throw new CustomException("There is some issue while exporting data");
        }
        return export;
    }

    // Save data into db
    public void saveData() throws CustomException {
        System.out.println("Save Data");
        String saveRows = "";
        for (Map.Entry<String, Post> postSet : posts.entrySet()) {
            Post tempPost = postSet.getValue();
            // System.out.println(tempPost.toString());
            PostExport tempExport = new PostExport(tempPost);
            // System.out.println(tempExport.toString2());
            saveRows = String.format("%s,(%s)", saveRows, tempExport.toString2());
        }
        if (saveRows.length() > 0)
            saveRows = saveRows.substring(1);
        // System.out.println(saveRows);
        try {
            dbService.deletePost("Post", "");
            dbService.addPost("Post", "INSERT INTO Post VALUES " + saveRows);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CustomException("There is some issue while saving data");
        }
    }
}