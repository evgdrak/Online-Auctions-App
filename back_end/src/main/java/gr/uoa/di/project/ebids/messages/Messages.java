package gr.uoa.di.project.ebids.messages;

import gr.uoa.di.project.ebids.user.User;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* * * * * * * * *
 * Messages entity
 * * * * * * * * */

@Entity
@Table(name = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "messageid", nullable = false)
    private Long id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Body")
    private String body;

    @Column(name = "Opened")
    private Boolean opened;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender", referencedColumnName = "userid")
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver", referencedColumnName = "userid")
    private User receiver;

    @Column(name = "timestamp")
    private Date timestamp;

    public Messages(){}

    /* Message to insert to database */
    public Messages(MessagesWS message) throws ParseException {
        this.id = message.getId();
        this.title = message.getTitle();
        this.body = message.getBody();
        this.opened = message.getOpened();
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(message.getTimestamp());
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
