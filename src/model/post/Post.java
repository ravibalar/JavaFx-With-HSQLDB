package model.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utility.CapacityException;
import utility.CustomException;
import utility.Status;

public abstract class Post {
	private String ID;
	private String title;
	private String description;
	private String imageName;
	private String creatorID;
	private Status status = Status.OPEN;
	private List<Reply> replies = new ArrayList<Reply>();

	public Post() {
		// TODO Auto-generated constructor stub
	}

	// New object of post and set instance variable
	public Post(String iD, String title, String description, String imageName, String creatorID) {
		super();
		this.ID = iD;
		this.title = title;
		this.description = description;
		this.creatorID = creatorID;
		this.imageName = imageName;
		this.status = Status.OPEN; // Default status OPEN
	}

	// Set all instace variable from data source
	public Post(String iD, String title, String description, String imageName, String creatorID, Status status, List<Reply> replies) {
		super();
		ID = iD;
		this.title = title;
		this.description = description;
		this.creatorID = creatorID;
		this.imageName = imageName;
		this.status = Objects.isNull(status) ? status : Status.OPEN;
		this.replies = replies;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	// Abstract method for Handle reply
	public abstract boolean handleReply(Reply reply) throws CustomException, CapacityException;

	// Abstract method for get post reply details
	public abstract String getReplyDetails();

	public String getPostMiniDetails() {
		// Get post limited details
		String postDetails = String.format("Title:\t %s \n" + "Description:\t %s", this.title, this.description);
		return postDetails;
	}

	public String getPostDetails() {
		// Get post details
		String postDetails = String.format(
				"ID:\t %s \n" + "Title:\t %s \n" + "Description:\t %s \n" + "Creator ID:\t %s \n" + "Status:\t %s",
				this.ID, this.title, this.description, this.creatorID, this.status);
		return postDetails;
	}

	@Override
	public String toString() {
		String str = String.format("%s,%s,%s,%s,%s", this.ID, this.title, this.description, this.creatorID,
				this.status);
		return str;
	}
}
