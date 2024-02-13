package gr.uoa.di.project.ebids.messages;

import java.text.SimpleDateFormat;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have message details
 * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class MessagesWS {

    private Long id;
    private String title;
    private String body;
    private Boolean opened;
    private String sender;
    private String receiver;
    private String timestamp;

    public MessagesWS(){}

    public MessagesWS(Messages message){
        this.id = message.getId();
        this.title = message.getTitle();
        this.body = message.getBody();
        this.opened = message.getOpened();
        this.sender = message.getSender().getUsername();
        this.receiver = message.getReceiver().getUsername();
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(message.getTimestamp());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
