package gr.uoa.di.project.ebids.item;

import java.util.ArrayList;
import java.util.List;

/* * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have items paged
 * * * * * * * * * * * * * * * * * * * * * * * * * */

public class ItemList {
    private Long count;
    private List<ItemWS> results = new ArrayList<>();

    public ItemList() {}

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<ItemWS> getResults() {
        return results;
    }

    public void setResults(List<ItemWS> results) {
        this.results = results;
    }
}
