package gr.uoa.di.project.ebids.item;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have category details
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CategoryWS {
    private Long id;
    private String name;

    public CategoryWS(){}

    public CategoryWS(Category category) {
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
}
