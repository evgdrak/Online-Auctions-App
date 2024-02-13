package gr.uoa.di.project.ebids.item;

import javax.persistence.*;

/* * * * * * * * * * * * * * * * * * * * * * *
 * Entity that connects items with categories
 * * * * * * * * * * * * * * * * * * * * * * */

@Entity
@Table(name = "item_category")
public class Item_Category {

    @EmbeddedId
    Item_Category_Key id;

    @ManyToOne
    @MapsId("categoryID")
    @JoinColumn(name = "categoryid")
    Category category;

    @ManyToOne
    @MapsId("itemID")
    @JoinColumn(name = "itemid")
    Item item;

    public Item_Category() {}

    public Item_Category(Item_Category_Key id) {
        this.id = id;
    }

    public Item_Category_Key getId() {
        return id;
    }

    public void setId(Item_Category_Key id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
