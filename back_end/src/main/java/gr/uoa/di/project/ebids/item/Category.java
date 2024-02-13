package gr.uoa.di.project.ebids.item;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/* * * * * * * * *
 * Category entity
 * * * * * * * * */

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "categoryid", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Item_Category> item_categories = new HashSet<>();

    public Category() {
    }

    public Category(CategoryWS category) {
        this.id = category.getId();
        this.name = category.getName();
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

    public Set<Item_Category> getItem_categories() {
        return item_categories;
    }

    public void setItem_categories(Set<Item_Category> item_categories) {
        this.item_categories = item_categories;
    }
}
