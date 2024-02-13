package gr.uoa.di.project.ebids.item;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/* * * * * * * * * * * * * * * *
 * Primary key of Item-Category
 * * * * * * * * * * * * * * * */

@Embeddable
public class Item_Category_Key implements Serializable {
    @Column(name = "itemid")
    Long itemID;

    @Column(name = "categoryid")
    Long categoryID;

    public Item_Category_Key(){}

    public Item_Category_Key(Long itemID, Long categoryID) {
        this.itemID = itemID;
        this.categoryID = categoryID;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }
}
