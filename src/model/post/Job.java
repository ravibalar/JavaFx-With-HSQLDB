package model.post;

import service.UniLinkConfig;
import utility.CustomException;
import utility.Status;

import java.util.Collections;
import java.util.List;

public class Job extends Post {
    private double proposedPrice;
    private double lowestOffer;
    private String responseUnit = UniLinkConfig.RESPONSE_UNIT;

    public Job() {
        // TODO Auto-generated constructor stub
    }

    public Job(String iD, String title, String description, String imageName, String creatorID, double proposedPrice) {
        super(iD, title, description, imageName, creatorID);
        this.proposedPrice = proposedPrice;
        this.lowestOffer = proposedPrice; // set as lowest offer initial
    }

    // Existing sale object initialize
    public Job(String iD, String title, String description, String imageName, String creatorID, Status status, List<Reply> replies,
               double proposedPrice, double lowestOffer) {
        super(iD, title, description, imageName, creatorID, status, replies);
        this.proposedPrice = proposedPrice;
        this.lowestOffer = lowestOffer;
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

    @Override
    public boolean handleReply(Reply reply) throws CustomException{
        // TODO Auto-generated method stub
        if (this.getStatus() != Status.CLOSED && reply.getValue() < this.proposedPrice) {
            if (reply.getValue() < this.lowestOffer || this.lowestOffer == 0) { // Set new offer if price is greater than current offer
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
                    this.lowestOffer = reply.getValue(); // set to highest offer
                    reply.setID(replyKey);
                    replies.add(reply);
                    super.setReplies(replies);
                    return true;
                } else {
                    System.out.println("Reply to job is already exist");
                    throw new CustomException("Reply to job is already exist");
                }
            } else {
                System.out.println("Job has lower offer than your offer");
                throw new CustomException("Job has lower offer than your offer");
            }
        } else {
            // Check for post status
            if (this.getStatus() == Status.CLOSED) {
                System.out.println("Job is closed");
            } else {
                System.out.println("Job is closed or value is greater than proposed prices");
                throw new CustomException("Job is closed or value is greater than proposed prices");
            }
        }
        return false; // rejected price if it is greater than proposed price or status = Closed
    }

    @Override
    public String getReplyDetails() {
        // Get reply details
        StringBuilder replyBuilder = new StringBuilder();
        replyBuilder.append(this.getPostDetails() + "\n");
        replyBuilder.append("--------Offer History -------" + "\n");
        if (this.getReplies().size() == 0) {
            // System.out.println("No Reply!!");
            replyBuilder.append("No Offer");
        } else {
            List<Reply> replies = this.getReplies();
            Collections.sort(replies, new SortReply());
            for (Reply reply : this.getReplies()) {
                replyBuilder.append(reply.getResponderID() + ":" + reply.getValue() + "\n");
            }
        }
        return replyBuilder.toString();
    }

    @Override
    public String getPostMiniDetails() {
        // Get post limited details
        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append(super.getPostMiniDetails() + "\n");
        postBuilder.append("Proposed Price:\t " + (responseUnit + this.proposedPrice) + "\n");
        postBuilder
                .append("Lowest Offer:\t " + (super.getReplies().size() == 0 ? "No Offer" : (responseUnit + this.lowestOffer)));
        return postBuilder.toString();
    }

    @Override
    public String getPostDetails() {
        // Get post details
        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append(super.getPostDetails() + "\n");
        postBuilder.append("Proposed Price:\t " + (responseUnit + this.proposedPrice) + "\n");
        postBuilder
                .append("Lowest Offer:\t " + (super.getReplies().size() == 0 ? "No Offer" : (responseUnit + this.lowestOffer)));
        return postBuilder.toString();
    }

    @Override
    public String toString() {
        String str = String.format("%s,%f,%f", super.toString(), this.proposedPrice, this.lowestOffer);
        return str;
    }
}
