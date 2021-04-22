package model.post;

public class Reply {
    private String ID;
    private double value;
    private String responderID;

    public Reply() {

    }

    public Reply(String ID, double value, String responderID) {
        // super();
        this.ID = ID;
        this.value = value;
        this.responderID = responderID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getResponderID() {
        return responderID;
    }

    public void setResponderID(String responderID) {
        this.responderID = responderID;
    }

    @Override
    public String toString() {
        return String.format("%s&%s&%s", ID, value, responderID);
    }

}
