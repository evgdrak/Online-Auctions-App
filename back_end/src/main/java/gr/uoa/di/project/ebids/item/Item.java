package gr.uoa.di.project.ebids.item;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import gr.uoa.di.project.ebids.bids.Bids;
import gr.uoa.di.project.ebids.user.User;

/* * * * * * *
 * Item entity
 * * * * * * */

@Entity
@Table(name = "item")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "itemid", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL)
    private Set<Item_Category> item_categories = new HashSet<>();

    @Column(name = "currently")
    private Double currently;

    @Column(name = "first_bid")
    private Double first_bid;

    @Column(name = "number_of_bids")
    private Integer number_of_bids;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "country")
    private String country;

    @Column(name = "started")
    private Date started;

    @Column(name = "ends")
    private Date ends;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "buy_price")
    private Double buy_price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_userid", referencedColumnName = "userid")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<Bids> bids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL)
    private Set<ItemPhotos> item_photos = new HashSet<>();

    public Item(){}

    public Item(ItemWS item) throws ParseException {
        this.name = item.getName();
        this.currently = item.getCurrently();
        this.first_bid = item.getFirst_bid();
        this.number_of_bids = item.getNumber_of_bids();
        this.location = item.getLocation();
        this.latitude = item.getLatitude();
        this.longitude = item.getLongitude();
        this.country = item.getCountry();
        this.started = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(item.getStarted());
        this.ends = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").parse(item.getEnds());
        this.description = item.getDescription();
        this.rating = item.getRating();
        this.buy_price = item.getBuy_price();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrently() {
        return currently;
    }

    public void setCurrently(Double currently) {
        this.currently = currently;
    }

    public Double getFirst_bid() {
        return first_bid;
    }

    public void setFirst_bid(Double first_bid) {
        this.first_bid = first_bid;
    }

    public Integer getNumber_of_bids() {
        return number_of_bids;
    }

    public void setNumber_of_bids(Integer number_of_bids) {
        this.number_of_bids = number_of_bids;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Double getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(Double buy_price) {
        this.buy_price = buy_price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Item_Category> getItem_categories() {
        return item_categories;
    }

    public void setItem_categories(Set<Item_Category> item_categories) {
        this.item_categories = item_categories;
    }

    public Set<Bids> getBids() {
        return bids;
    }

    public void setBids(Set<Bids> bids) {
        this.bids = bids;
    }

    public Set<ItemPhotos> getItem_photos() {
        return item_photos;
    }

    public void setItem_photos(Set<ItemPhotos> item_photos) {
        this.item_photos = item_photos;
    }

}

