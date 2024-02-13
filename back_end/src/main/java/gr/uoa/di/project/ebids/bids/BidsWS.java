package gr.uoa.di.project.ebids.bids;

import java.text.SimpleDateFormat;

/* * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have bids details
 * * * * * * * * * * * * * * * * * * * * * * * * * */

public class BidsWS {
    private long id;
    private String time;
    private double amount;
    private Integer rating;
    private String userID;
    private long item;

    public BidsWS(){}

    public BidsWS(Bids bid){
        this.id = bid.getId();
        this.time = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(bid.getTime());
        this.amount = bid.getAmount();
        this.rating = bid.getRating();
        this.userID = bid.getUser().getUsername();
        this.item = bid.getItem().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getItem() {
        return item;
    }

    public void setItem(long item) {
        this.item = item;
    }
}
