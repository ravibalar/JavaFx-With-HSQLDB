package service.post;

import model.post.Event;
import model.post.Reply;
import model.user.User;
import service.UniLinkConfig;
import utility.CapacityException;
import utility.CustomException;
import utility.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventService {
    private static final String ID_PREFIX = "EVE";
    private Map<String, Event> events = new HashMap<String, Event>();

    public EventService() {
        // Initialize the list of events
    }

    // Create new eventid
    public String createID() {
        int size = events.size() + 1;
        String sizeID = (size < 10 ? "00" : (size >= 10 && size <= 99 ? "0" : "")) + size;
        return ID_PREFIX + (sizeID);
    }

    // Replace existing event with new
    public void replace(Event oldEvent, Event newEvent) {
        events.remove(oldEvent.getID()); // Remove old event
        events.put(newEvent.getID(), newEvent); // Add new event
    }

    // Replace existing event by ID
    public void replaceByID(String ID, Event newEvent) {
        events.remove(ID); // Remove old event
        events.put(ID, newEvent); // Add new event
    }

    // Post new event
    public Event post(Event event) {
        // Check if object has id
        String eventID = event.getID() != null || event.getID().contains("TEMP") ? createID() : event.getID(); // create new ID
        event.setID(eventID);
        events.put(eventID, event);
        return event;
    }

    // Post new event
    public Event postParse(String[] dataStr) {
        Event tempEvent = new Event();
        try {
            System.out.println("Event Parse string length:" + dataStr.length);
            tempEvent.setID(dataStr[1]);
            tempEvent.setTitle(dataStr[2]);
            tempEvent.setDescription(dataStr[3]);
            String imageName = (dataStr[4].length() == 0 || dataStr[4] == null || dataStr[4].trim().replace("null", "").equalsIgnoreCase("")) ? UniLinkConfig.DEFAULT_SUMMARY_IMAGE : dataStr[4];
            tempEvent.setImageName(imageName);
            tempEvent.setCreatorID(dataStr[5]);
            tempEvent.setStatus(Status.valueOf(dataStr[6].toUpperCase()));
            tempEvent.setVenue(dataStr[7]);
            tempEvent.setDate(dataStr[8]);
            tempEvent.setCapacity(Integer.parseInt(dataStr[9]));
            List<Reply> tempList = new ArrayList<Reply>();
            if (dataStr.length == 14 && dataStr[13] != null && dataStr[13].length() > 0) {
                String[] replyStr = dataStr[13].split("\\|");
                System.out.println("Reply Length:" + dataStr[13].split("\\|").length);
                for (int i = 0; i < replyStr.length; i++) {
                    String[] reply = replyStr[i].replace("{", "").replace("}", "").split("&");
                    tempList.add(new Reply(reply[0], Double.parseDouble(reply[1]), reply[2]));
                    // System.out.println(reply[0] + ":" + reply[1] + ":" + reply[2]);
                }
                tempEvent.setAttendeeCount(tempList.size());
                if (tempEvent.getAttendeeCount() == tempEvent.getCapacity()) tempEvent.setStatus(Status.CLOSED);
            }
            tempEvent.setReplies(tempList);
        } catch (Exception ex) {
            System.out.println("EventParse:" + ex.toString());
            tempEvent = null;
            throw ex;
        }
        return tempEvent;
    }

    // Validate reply
    public List<Reply> resetReply(Event event) {
        Event tempPost = events.get(event.getID());
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
            events.put(tempPost.getID(), tempPost);
        }
        return tempPost.getReplies();
    }

    // Reply to existing event
    public boolean reply(Event event, Reply reply) throws CapacityException, CustomException {
        Event tempEvent = events.get(event.getID());
        if (tempEvent != null) {
            tempEvent.handleReply(reply);
            events.put(tempEvent.getID(), tempEvent);
            return true;
        } else {
            System.out.println("No event found for reply");
            // throw new NotFoundException("No event found for reply");
        }
        return false;
    }

    // Close ecsiting event
    public Event close(Event e, User user) {
        String closeID = e.getID();
        if (closeID != null && closeID.length() > 0) {
            closeID = closeID.toUpperCase();
            Event tempEvent = events.get(closeID);
            // Check for event creator = current user
            if (tempEvent != null && tempEvent.getCreatorID().equalsIgnoreCase(user.getID())) {
                // Close event if current user is creator of post
                if (tempEvent.getStatus() == Status.CLOSED) {
                    System.out.println("Post is already closed");
                } else {
                    // Set status to closed
                    tempEvent.setStatus(Status.CLOSED);
                    events.replace(tempEvent.getID(), tempEvent);
                }
                return tempEvent;
            } else {
                System.out.println("No Post found");
            }
        }
        return null;
    }

    // Delete existing event
    public boolean delete(Event e, User user) {
        String deleteID = e.getID();
        if (deleteID != null && deleteID.length() > 0) {
            deleteID = deleteID.toUpperCase();
            Event tempEvent = events.get(deleteID);
            // Check event creator = current user
            if (tempEvent != null && tempEvent.getCreatorID().equalsIgnoreCase(user.getID())) {
                // Delete event if current user is creator of post
                events.remove(tempEvent.getID());
                return true;
            } else {
                System.out.println("No Post found");
            }
        }
        return false;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    // Get event by ID
    public Event getEvent(String ID) {
        return events.get(ID);
    }
}
