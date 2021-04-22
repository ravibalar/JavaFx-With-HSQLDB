package model.post;

import service.UniLinkConfig;
import utility.CustomException;
import utility.Status;

import java.util.Collections;
import java.util.List;

public class Sale extends Post {
    private double askingPrice;
    private double minimumRaise;
    private double highestOffer = 0;
    private String responseUnit = UniLinkConfig.RESPONSE_UNIT;
    public Sale() {
        // TODO Auto-generated constructor stub
    }

    // New sale object initialize
    public Sale(String iD, String title, String description, String imageName, String creatorID, double askingPrice,
                double minimumRaise) {
        super(iD, title, description, imageName, creatorID);
        this.askingPrice = askingPrice;
        this.highestOffer = 0;
        this.minimumRaise = minimumRaise;
    }

    // Existing sale object initialize
    public Sale(String iD, String title, String description, String imageName, String creatorID, Status status, List<Reply> replies,
                double askingPrice, double minimumRaise, double highestOffer) {
        super(iD, title, description, creatorID, imageName, status, replies);
        this.askingPrice = askingPrice;
        this.highestOffer = highestOffer;
        this.minimumRaise = minimumRaise;
    }

    public double getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    public double getHighestOffer() {
        return highestOffer;
    }

    public void setHighestOffer(double highestOffer) {
        this.highestOffer = highestOffer;
    }

    public double getMinimumRaise() {
        return minimumRaise;
    }

    public void setMinimumRaise(double minimumRaise) {
        this.minimumRaise = minimumRaise;
    }

    @Override
    public boolean handleReply(Reply reply) throws CustomException{
        // Handle post reply
        if (this.getStatus() != Status.CLOSED) {
            if (reply.getValue() >= this.highestOffer + this.minimumRaise) { // Set new highest offer if price is
                // greater
                // than current offer
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
                    this.highestOffer = reply.getValue(); // set to highest offer
                    reply.setID(replyKey);
                    replies.add(reply);
                    super.setReplies(replies);
                    // Closed sale for reply price if it is >= asking price
                    if (this.highestOffer > this.askingPrice) {
                        super.setStatus(Status.CLOSED); // Set post status
                        System.out.println("Congratulations! Item is sold");
                    }
                    return true;
                } else {
                    System.out.println("Reply to Sale is already exist");
                    throw new CustomException("Reply to Sale is already exist");
                }
            } else { // rejected price if it is less than highest offer with minimum raise
                System.out.println("Reply value should have minimum raise in current highest offer of post");
                throw new CustomException("Reply value should have minimum raise in current highest offer of post");
            }
        } else {
            if (this.getStatus() == Status.CLOSED) {
                System.out.println("Sale post is closed");
                throw new CustomException("Sale post is closed");
            }
        }
        return false; // rejected price if post status = Closed
    }

    @Override
    public String getReplyDetails() {
        // Get post reply details
        StringBuilder replyBuilder = new StringBuilder();
        replyBuilder.append(this.getPostDetails() + "\n");
        replyBuilder.append("Asking Price:\t$" + this.askingPrice + "\n");
        replyBuilder.append("--------Offer History -------" + "\n");
        // Check for no of reply
        if (this.getReplies().size() == 0) {
            replyBuilder.append("No Offer");
        } else {
            List<Reply> replies = this.getReplies();
            Collections.sort(replies, new SortReplyDesc());
            for (Reply reply : this.getReplies()) {
                replyBuilder.append(reply.getResponderID() + ":" + reply.getValue() + "\n");
            }
        }
        return replyBuilder.toString();
    }

    @Override
    public String getPostMiniDetails() {
        // Get post mini details
        // Get post details
        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append(super.getPostMiniDetails() + "\n");
        postBuilder.append("Minimum Raise:\t " + (responseUnit + this.minimumRaise ) + "\n");
        postBuilder.append(
                "Highest Offer:\t " + (super.getReplies().size() == 0 ? "No Offer" : (responseUnit + this.highestOffer)));
        return postBuilder.toString();
    }

    @Override
    public String getPostDetails() {
        // Get post details
        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append(super.getPostDetails() + "\n");
        postBuilder.append("Minimum Raise:\t " + (responseUnit + this.minimumRaise ) + "\n");
        postBuilder.append(
                "Highest Offer:\t " + (super.getReplies().size() == 0 ? "No Offer" : (responseUnit + this.highestOffer)));
        return postBuilder.toString();
    }

    @Override
    public String toString() {
        String str = String.format("%s,%f,%f,%f", super.toString(), this.askingPrice, this.minimumRaise,
                this.highestOffer);
        return str;
    }
}
