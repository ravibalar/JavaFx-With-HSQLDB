package model.post;

import utility.Status;

import java.util.ArrayList;
import java.util.List;

public class PostExport {
    private String postType;
    private String ID;
    private String title;
    private String description;
    private String imageName;
    private String creatorID;
    private Status status;// = Status.OPEN;
    private String venue;
    private String date;
    private int capacity;
    private int attendeeCount;
    private double askingPrice;
    private double minimumRaise;
    private double highestOffer;
    private double proposedPrice;
    private double lowestOffer;
    private List<Reply> replies = new ArrayList<Reply>();
    private Event event = null;
    private Sale sale = null;
    private Job job = null;

    public PostExport(String postType, String ID, String title, String description, String imageName, String creatorID, Status status, String venue, String date, int capacity, double askingPrice, double minimumRaise, double proposedPrice) {
        this.postType = postType;
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.creatorID = creatorID;
        this.status = status;
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.askingPrice = askingPrice;
        this.minimumRaise = minimumRaise;
        this.proposedPrice = proposedPrice;
    }

    public PostExport(Post post) {
        if (post instanceof Event) {
            this.setPost((Event) post);
        } else if (post instanceof Sale) {
            this.setPost((Sale) post);
        } else {
            this.setPost((Job) post);
        }
        System.out.println("Any Reply?:" + post.getReplies().size());
        this.setReplies(post.getReplies());
    }

    public void setPost(Event event) {
        System.out.println("set Event");
        this.event = event;
        this.postType = "EVENT";
        this.ID = event.getID();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.imageName = event.getImageName();
        this.creatorID = event.getCreatorID();
        this.status = event.getStatus();
        this.venue = event.getVenue();
        this.date = event.getDate();
        this.capacity = event.getCapacity();
    }

    public void setPost(Sale sale) {
        System.out.println("set Sale");
        this.sale = sale;
        this.postType = "SALE";
        this.ID = sale.getID();
        this.title = sale.getTitle();
        this.description = sale.getDescription();
        this.imageName = sale.getImageName();
        this.creatorID = sale.getCreatorID();
        this.status = sale.getStatus();
        this.askingPrice = sale.getAskingPrice();
        this.minimumRaise = sale.getMinimumRaise();
    }

    public void setPost(Job job) {
        System.out.println("set Job");
        this.job = job;
        this.postType = "JOB";
        this.ID = job.getID();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.imageName = job.getImageName();
        this.creatorID = job.getCreatorID();
        this.status = job.getStatus();
        this.proposedPrice = job.getProposedPrice();
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public double getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    public double getMinimumRaise() {
        return minimumRaise;
    }

    public void setMinimumRaise(double minimumRaise) {
        this.minimumRaise = minimumRaise;
    }

    public double getHighestOffer() {
        return highestOffer;
    }

    public void setHighestOffer(double highestOffer) {
        this.highestOffer = highestOffer;
    }

    public double getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(double proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public double getLowestOffer() {
        return lowestOffer;
    }

    public void setLowestOffer(double lowestOffer) {
        this.lowestOffer = lowestOffer;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<Reply> stringToReplies(String replyStr) {
        String[] reply = replyStr.split("\\|");
        List<Reply> replyList = new ArrayList<>();
        for(int i =0;i<reply.length;i++){
            System.out.println(reply[0]);
        }
        return replyList;
    }

    public String repliesToString() {
        String replyStr = "";
        for (Reply r : replies) {
            replyStr = String.format("%s|{%s}", replyStr, r.toString());
        }
        if (replyStr.length() > 0) replyStr = replyStr.substring(1);
        return replyStr;
    }

    @Override
    public String toString() {
        String str = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", this.postType, this.ID, this.title, this.description, this.imageName, this.creatorID,
                this.status, this.venue, this.date, this.capacity, this.askingPrice, this.minimumRaise, this.proposedPrice, this.repliesToString());
        return str;
    }

    public String toString2() {
        String str = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s',%s,%s,%s,%s,'%s'",
                this.postType, this.ID, this.title, this.description, this.imageName, this.creatorID,
                this.status, this.venue, this.date, this.capacity, this.askingPrice, this.minimumRaise, this.proposedPrice, this.repliesToString());
        return str;
    }
}
