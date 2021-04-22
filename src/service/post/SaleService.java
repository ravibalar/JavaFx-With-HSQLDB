package service.post;

import model.post.Job;
import model.post.Reply;
import model.post.Sale;
import model.post.SortReplyDesc;
import model.user.User;
import service.UniLinkConfig;
import utility.CustomException;
import utility.Status;

import java.util.*;

public class SaleService {
    private static final String ID_PREFIX = "SAL";
    private Map<String, Sale> sales = new HashMap<String, Sale>();

    public SaleService() {
        // Initialize the list of sales
    }

    // Create new Sale id
    public String createID() {
        int size = sales.size() + 1;
        String sizeID = (size < 10 ? "00" : (size >= 10 && size <= 99 ? "0" : "")) + size;
        return ID_PREFIX + (sizeID);
    }

    // Replace existing sale with new
    public void replace(Sale oldSale, Sale newSale) {
        sales.remove(oldSale.getID()); // Remove old sale
        sales.put(newSale.getID(), newSale); // Add new sale
    }

    // Post new sale
    public Sale post(Sale sale) {
        // Check if object has id
        String saleID = sale.getID() != null || sale.getID().contains("TEMP") ? createID() : sale.getID(); // create new ID
        sale.setID(saleID);
        sales.put(saleID, sale);
        return sale;
    }

    public Sale postParse(String[] dataStr) {
        Sale tempSale = new Sale();
        try {
            System.out.println("Sales Parse string length:" + dataStr.length);
            tempSale.setID(dataStr[1]);
            tempSale.setTitle(dataStr[2]);
            tempSale.setDescription(dataStr[3]);
            String imageName = (dataStr[4].length() == 0 || dataStr[4] == null || dataStr[4].trim().replace("null", "").equalsIgnoreCase("")) ? UniLinkConfig.DEFAULT_SUMMARY_IMAGE : dataStr[4];
            tempSale.setImageName(imageName);
            tempSale.setCreatorID(dataStr[5]);
            tempSale.setStatus(Status.valueOf(dataStr[6].toUpperCase()));
            tempSale.setAskingPrice(Double.parseDouble(dataStr[10]));
            tempSale.setMinimumRaise(Double.parseDouble(dataStr[11]));
            List<Reply> tempList = new ArrayList<Reply>();
            if (dataStr.length == 14 && dataStr[13] != null && dataStr[13].length() > 0) {
                String[] replyStr = dataStr[13].split("\\|");
                System.out.println("Reply Length:" + dataStr[13].split("\\|").length);
                for (int i = 0; i < replyStr.length; i++) {
                    String[] reply = replyStr[i].replace("{","").replace("}","").split("&");
                    tempList.add(new Reply(reply[0], Double.parseDouble(reply[1]), reply[2]));
                    // System.out.println(reply[0] + ":" + reply[1] + ":" + reply[2]);
                }
                Collections.sort(tempList, new SortReplyDesc());
                tempSale.setHighestOffer(tempList.get(0).getValue());
            }
            tempSale.setReplies(tempList);
        } catch (Exception ex) {
            System.out.println("SaleParse:"+ex.toString());
            tempSale = null;
            throw ex;
        }
        return tempSale;
    }

    // Validate reply
    public List<Reply> resetReply(Sale sale) {
        Sale tempPost = sales.get(sale.getID());
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
            sales.put(tempPost.getID(), tempPost);
        }
        return tempPost.getReplies();
    }

    // Reply to existing sale
    public boolean reply(Sale sale, Reply reply) throws CustomException {
        Sale tempSale = sales.get(sale.getID());
        if (tempSale != null) {
            tempSale.handleReply(reply);
            sales.put(tempSale.getID(), tempSale);
            return true;
        } else {
            System.out.println("No sale found for reply");
        }
        return false;
    }

    // Close existing sale
    public Sale close(Sale e, User user) {
        String closeID = e.getID();
        if (closeID != null && closeID.length() > 0) {
            closeID = closeID.toUpperCase();
            Sale tempSale = sales.get(closeID);
            // Check sale creator = current user
            if (tempSale != null && tempSale.getCreatorID().equalsIgnoreCase(user.getID())) {
                // Close sale if current user is creator of post
                tempSale.setStatus(Status.CLOSED);
                sales.replace(tempSale.getID(), tempSale);
                return tempSale;
            } else {
                System.out.println("No Post found");
            }
        }
        return null;
    }

    // Delete existing sale
    public boolean delete(Sale e, User user) {
        String deleteID = e.getID();
        if (deleteID != null && deleteID.length() > 0) {
            deleteID = deleteID.toUpperCase();
            Sale tempSale = sales.get(deleteID);
            // Check sale creator = current user
            if (tempSale != null && tempSale.getCreatorID().equalsIgnoreCase(user.getID())) {
                // Delete sale if current user is creator of post
                sales.remove(tempSale.getID());
                return true;
            } else {
                System.out.println("No Post found");
            }
        }
        return false;
    }

    public Map<String, Sale> getSales() {
        return sales;
    }

    public void setSales(Map<String, Sale> sales) {
        this.sales = sales;
    }

    // Sale by Id
    public Sale getSale(String ID) {
        return sales.get(ID);
    }
}
