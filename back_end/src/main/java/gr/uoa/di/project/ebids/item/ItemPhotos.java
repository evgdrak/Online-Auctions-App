package gr.uoa.di.project.ebids.item;

import javax.persistence.*;

/* * * * * * * * * * * * * * * * * * * * *
 * Entity that connects items with photos
 * * * * * * * * * * * * * * * * * * * * */

@Entity
@Table(name = "item_photos")
public class ItemPhotos {

    @EmbeddedId
    ItemPhotosPK id;

    @ManyToOne
    @MapsId("photoID")
    @JoinColumn(name = "photos_photoID")
    Photos photo;

    @ManyToOne
    @MapsId("itemID")
    @JoinColumn(name = "itemid")
    Item item;

    public ItemPhotos() {}

    public ItemPhotos(ItemPhotosPK id){ this.id = id; }

    public ItemPhotosPK getId(){ return id; }

    public void setId(ItemPhotosPK id) { this.id = id;}

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Photos getPhoto() {
        return photo;
    }

    public void setPhoto(Photos photo) {
        this.photo = photo;
    }


}
