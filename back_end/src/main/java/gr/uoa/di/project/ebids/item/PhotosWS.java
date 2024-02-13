package gr.uoa.di.project.ebids.item;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have photos details
 * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class PhotosWS {

    private Long id;
    private String name;
    private String type;
    private byte[] image;
    private ItemPhotos item_photo;

    public PhotosWS(){}
    public PhotosWS(Photos photo) {
        this.id = photo.getId();
        this.name = photo.getName();
        this.image = photo.getImage();
        this.type = photo.getType();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ItemPhotos getItem_photo() {
        return item_photo;
    }

    public void setItem_photo(ItemPhotos item_photo) {
        this.item_photo = item_photo;
    }


}
