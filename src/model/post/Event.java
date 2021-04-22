package model.post;

import utility.CapacityException;
import utility.CustomException;
import utility.Status;

import java.util.List;

public class Event extends Post {
    private String venue;
    private String date;
    private int capacity;
    private int attendeeCount;

    public Event() {

    }

    // New event object initialize
    public Event(String iD, String title, String description, String imageName, String creatorID, String venue, String date,
                 int capacity) {
        super(iD, title, description, imageName, creatorID); // Set super class constructor
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = 0; // Initialize attendee count to 0
    }

    // Existing event object initialize
    public Event(String iD, String title, String description, String imageName, String creatorID, Status status, List<Reply> replies,
                 String venue, String date, int capacity, int attendeeCount) {
        super(iD, title, description, imageName, creatorID, status, replies);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = attendeeCount;
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

    // Handle reply to event
    @Override
    public boolean handleReply(Reply reply) throws CustomException, CapacityException {
        // TODO Auto-generated method stub
        if (reply.getValue() + 1 == 2) { // Check entered value is greater than 0
            // Check capacity is greater than current attendeeCount and status of event is
            // not closed
            if (this.getStatus() != Status.CLOSED && this.capacity > this.attendeeCount) {
                List<Reply> replies = super.getReplies();
                String replyKey = reply.getID() + "-" + reply.getResponderID();
                // Check if student reply is already exist
                boolean isAlreadyExist = false;
                for (Reply r : replies) {
                    if (r.getID() != null && r.getID().equalsIgnoreCase(replyKey)) {
                        isAlreadyExist = true;
                    }
                }
                // Check for post reply
                if (!isAlreadyExist) {
                    reply.setID(replyKey);
                    // reply.setValue(1);
                    replies.add(reply);
                    this.attendeeCount++;
                    super.setReplies(replies);
                    // Closed the event if the attendee count = capacity

//                    System.out.println("Event capacity:" + this.capacity);
//                    System.out.println("Event Current Attendee:" + this.attendeeCount);
                    if (this.capacity == this.attendeeCount) {
                        super.setStatus(Status.CLOSED); // update post status
                        // System.out.println("Event list is full!");
                    }
                    return true;
                } else {
                    System.out.println("Reply to Event is already exist !");
                    throw new CustomException("Reply to Event is already exist !");
                }
            } else {
                // Check for post status
                if (this.getStatus() == Status.CLOSED) {
                    System.out.println("Event is closed");
                    throw new CustomException("Event is closed");
                } else {
                    throw new CapacityException("Event is full");
                }
            }
        } else {
            System.out.println("Reply Value should be 1");
        }
        return false;
    }

    @Override
    public String getReplyDetails() {
        // Get post reply details
        StringBuilder replyBuilder = new StringBuilder();
        replyBuilder.append(this.getPostDetails() + "\n");
        replyBuilder.append("Attendee list:\t");
        String replies = "Empty";
        // Check for no of reply
        if (this.getReplies().size() > 0) {
            replies = "";
            for (Reply reply : this.getReplies()) {
                // Reply reply = replySet.getValue();
                replies += reply.getResponderID() + ",";
            }
            replies = replies.substring(0, replies.length() - 1);
        }
        replyBuilder.append(replies);
        return replyBuilder.toString();
    }

    @Override
    public String getPostMiniDetails() {
        // Get post limited details
        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append(super.getPostMiniDetails() + "\n");
        postBuilder.append("Venue:\t " + this.venue + "\n");
        postBuilder.append("Date:\t " + this.date + "\n");
        postBuilder.append("Capacity:\t " + this.capacity);
        return postBuilder.toString();
    }

    @Override
    public String getPostDetails() {
        // Get post details
        // Get post limited details
        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append(super.getPostMiniDetails() + "\n");
        postBuilder.append("Venue:\t " + this.venue + "\n");
        postBuilder.append("Date:\t " + this.date + "\n");
        postBuilder.append("Capacity:\t " + this.capacity + "\n");
        postBuilder.append("Attendees:\t " + this.attendeeCount);
        return postBuilder.toString();
    }

    @Override
    public String toString() {
        String str = String.format("%s,%s,%s,%d,%d", super.toString(), this.venue, this.date, this.capacity,
                this.attendeeCount);
        return str;
    }
}
