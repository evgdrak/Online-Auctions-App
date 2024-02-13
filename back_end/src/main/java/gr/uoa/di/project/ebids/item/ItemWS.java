package gr.uoa.di.project.ebids.item;

import gr.uoa.di.project.ebids.bids.Bids;
import gr.uoa.di.project.ebids.bids.BidsWS;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/* * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have item details
 * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class ItemWS {

    private long id;
    private String name;
    private Set<CategoryWS> categories = new HashSet<>();
    private Double currently;
    private Double first_bid;
    private Integer number_of_bids;
    private String location;
    private Double latitude;
    private Double longitude;
    private String country;
    private String started;
    private String ends;
    private String description;
    private Integer rating;
    private Double buy_price;
    private String userID;

    private Set<BidsWS> bids = new HashSet<>();
    private Set<PhotosWS> photos = new HashSet<>();

    public ItemWS(){}

    public ItemWS(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.currently = item.getCurrently();
        this.first_bid = item.getFirst_bid();
        this.number_of_bids = item.getNumber_of_bids();
        this.location = item.getLocation();
        this.latitude = item.getLatitude();
        this.longitude = item.getLongitude();
        this.location = item.getLocation();
        this.country = item.getCountry();
        this.started = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(item.getStarted());
        this.ends = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").format(item.getEnds());
        this.description = item.getDescription();
        this.rating = item.getRating();
        this.buy_price = item.getBuy_price();
        this.userID = item.getUser().getUsername();

        for(Item_Category item_category : item.getItem_categories()){
            this.categories.add(new CategoryWS(item_category.getCategory()));
        }

        for(ItemPhotos item_photo : item.getItem_photos()){
            this.photos.add(new PhotosWS(item_photo.getPhoto()));
        }

        for(Bids bid : item.getBids()){
            this.bids.add(new BidsWS(bid));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CategoryWS> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryWS> categories) {
        this.categories = categories;
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

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Set<PhotosWS> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<PhotosWS> photos) {
        this.photos = photos;
    }

    public Set<BidsWS> getBids() {
        return bids;
    }

    public void setBids(Set<BidsWS> bids) {
        this.bids = bids;
    }
}
