package gr.uoa.di.project.ebids.bids;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import gr.uoa.di.project.ebids.item.Item;
import gr.uoa.di.project.ebids.user.User;

/* * * * * * *
 * Bids entity
 * * * * * * */

@Entity
@Table(name = "bids")
public class Bids {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "bidsid")
    private long id;

    @Column(name = "time")
    private Date time;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "rating")
    private Integer rating;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_userid", referencedColumnName = "userid")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_itemid", referencedColumnName = "itemid")
    private Item item;

    public Bids(){}

    public Bids(BidsWS bid) throws ParseException {
        this.id = bid.getId();
        this.time = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(bid.getTime());
        this.amount = bid.getAmount();
        this.rating = bid.getRating();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

