package gr.uoa.di.project.ebids.item;

import javax.persistence.*;
import java.io.Serializable;

/* * * * * * * * * * * * * * *
 * Primary key of Item-Photos
 * * * * * * * * * * * * * * */

@Embeddable
public class ItemPhotosPK implements Serializable {

    @Column(name = "itemid")
    Long itemID;

    @Column(name = "photos_photoID")
    Long photoID;

    public ItemPhotosPK(){}

    public ItemPhotosPK(Long itemID, Long photoID){
        this.itemID = itemID;
        this.photoID = photoID;
    }

    public Long getItemId() {
        return itemID;
    }

    public void setItemId(Long itemId) {
        this.itemID = itemId;
    }

    public Long getPhotoId() {
        return photoID;
    }

    public void setPhotoId(Long photoId) {
        this.photoID = photoId;
    }


}
