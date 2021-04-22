package service.post;

import model.post.Job;
import model.post.Reply;
import model.post.SortReply;
import model.user.User;
import service.UniLinkConfig;
import utility.CustomException;
import utility.Status;

import java.util.*;

public class JobService {
    private static final String ID_PREFIX = "JOB";
    private Map<String, Job> jobs = new HashMap<String, Job>();

    public JobService() {
        // Initialize the list of jobs
    }

    // Create new job id
    public String createID() {
        int size = jobs.size() + 1;
        String sizeID = (size < 10 ? "00" : (size >= 10 && size <= 99 ? "0" : "")) + size;
        return ID_PREFIX + (sizeID);
    }

    // Replace existin event with new
    public void replace(Job oldJob, Job newJob) {
        jobs.remove(oldJob.getID()); // Remove old job
        jobs.put(newJob.getID(), newJob); // Add new job
    }

    // Post new job
    public Job post(Job job) {
        // Check if object has id
        String jobID = job.getID() != null || job.getID().contains("TEMP") ? createID() : job.getID(); // create new ID
        job.setID(jobID);
        jobs.put(jobID, job);
        return job;
    }

    public Job postParse(String[] dataStr) {
        Job tempJob = new Job();
        try {
            System.out.println("Job Parse string length:" + dataStr.length);
            tempJob.setID(dataStr[1]);
            tempJob.setTitle(dataStr[2]);
            tempJob.setDescription(dataStr[3]);
            String imageName = (dataStr[4].length() == 0 || dataStr[4] == null || dataStr[4].trim().replace("null", "").equalsIgnoreCase("")) ? UniLinkConfig.DEFAULT_SUMMARY_IMAGE : dataStr[4];
            tempJob.setImageName(imageName);
            tempJob.setCreatorID(dataStr[5]);
            tempJob.setStatus(Status.valueOf(dataStr[6].toUpperCase()));
            tempJob.setProposedPrice(Double.parseDouble(dataStr[12]));
            List<Reply> tempList = new ArrayList<Reply>();

            if (dataStr.length == 14 && dataStr[13] != null && dataStr[13].length() > 0) {
                String[] replyStr = dataStr[13].split("\\|");
                System.out.println("Reply Length:" + dataStr[13].split("\\|").length);
                for (int i = 0; i < replyStr.length; i++) {
                    String[] reply = replyStr[i].replace("{", "").replace("}", "").split("&");
                    tempList.add(new Reply(reply[0], Double.parseDouble(reply[1]), reply[2]));
                    // System.out.println(reply[0] + ":" + reply[1] + ":" + reply[2]);
                }
                Collections.sort(tempList, new SortReply());
                tempJob.setLowestOffer(tempList.get(0).getValue());
            }
            tempJob.setReplies(tempList);
        } catch (Exception ex) {
            System.out.println("JobParse:" + ex.toString());
            tempJob = null;
            throw ex;
        }
        return tempJob;
    }

    // Validate reply
    public List<Reply> resetReply(Job job) {
        Job tempPost = jobs.get(job.getID());
        if (tempPost != null) {
            List<Reply> replies = new ArrayList<>();
            //Collections.sort(replies, SortReplyDesc());
            for (Reply tempReply : tempPost.getReplies()) {
                try {
                    tempReply.setID(tempPost.getID() + "-" + tempReply.getResponderID());
                    replies.add(tempReply);
                } catch (Exception ex) {
                    System.out.println("Import Reply:" + ex.toString());
                }
            }
            jobs.put(tempPost.getID(), tempPost);
        }
        return tempPost.getReplies();
    }

    // Reply to existing job
    public boolean reply(Job job, Reply reply) throws CustomException {
        Job tempJob = jobs.get(job.getID());
        if (tempJob != null) {
            tempJob.handleReply(reply);
            jobs.put(tempJob.getID(), tempJob);
            return true;
        } else {
            System.out.println("No job found for reply");
        }
        return false;
    }

    // Close existing job
    public Job close(Job e, User user) {
        String closeID = e.getID();
        if (closeID != null && closeID.length() > 0) {
            closeID = closeID.toUpperCase();
            Job tempJob = jobs.get(closeID);
            // Check job creator = current user
            if (tempJob != null && tempJob.getCreatorID().equalsIgnoreCase(user.getID())) {
                // Close job if current user is creator of post
                tempJob.setStatus(Status.CLOSED);
                jobs.replace(tempJob.getID(), tempJob);
                return tempJob;
            } else {
                System.out.println("No Post found");
            }
        }
        return null;
    }

    // Delete existing job
    public boolean delete(Job e, User user) {
        String deleteID = e.getID();
        if (deleteID != null && deleteID.length() > 0) {
            deleteID = deleteID.toUpperCase();
            Job tempJob = jobs.get(deleteID);
            // Check job creator = current user
            if (tempJob != null && tempJob.getCreatorID().equalsIgnoreCase(user.getID())) {
                // Delete job if current user is creator of post
                jobs.remove(tempJob.getID());
                return true;
            } else {
                System.out.println("No Post found");
            }
        }
        return false;
    }

    public Map<String, Job> getJobs() {
        return jobs;
    }

    public void setJobs(Map<String, Job> jobs) {
        this.jobs = jobs;
    }

    // Job by ID
    public Job getJob(String ID) {
        return jobs.get(ID);
    }
}
