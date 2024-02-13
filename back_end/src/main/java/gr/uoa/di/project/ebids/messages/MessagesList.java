package gr.uoa.di.project.ebids.messages;

import java.util.ArrayList;
import java.util.List;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have messages paged
 * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class MessagesList {
    private Long count;
    private List<MessagesWS> results = new ArrayList<>();

    public MessagesList() {}

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<MessagesWS> getResults() {
        return results;
    }

    public void setResults(List<MessagesWS> results) {
        this.results = results;
    }
}
