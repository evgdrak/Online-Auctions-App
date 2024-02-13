package gr.uoa.di.project.ebids.messages;

import gr.uoa.di.project.ebids.item.*;
import gr.uoa.di.project.ebids.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/* * * * * * * * * * * * * * * * * * *
 * Functionalities for messages entity
 * * * * * * * * * * * * * * * * * * */

@Service
public class MessagesService {
    @Autowired
    MessagesDAO messagesDAO;

    @Autowired
    UserDAO userDAO;

    // Get limit number of messages from page number = page
    public MessagesList getMessages(Integer page, Integer limit){
        MessagesList messagesList = new MessagesList();
        List<MessagesWS> messages = new ArrayList<>();

        messagesList.setCount(messagesDAO.getAllMessagesCount());

        for(Messages message: messagesDAO.getAllMessages(page, limit)){
            messages.add(new MessagesWS(message));
        }

        messagesList.setResults(messages);

        return messagesList;
    }

    // Get messages of user
    public MessagesList getMessagesOfUser(Map<String, String> parameters, String username, Boolean send){
        MessagesList messagesList = new MessagesList();
        List<MessagesWS> messages = new ArrayList<>();

        messagesList.setCount(messagesDAO.getMessagesCount(parameters, username, send));

        for(Messages message: messagesDAO.getMessages(parameters, username, send)){
            messages.add(new MessagesWS(message));
        }

        messagesList.setResults(messages);

        return messagesList;
    }

    // Get unread messages of user
    public Long getUnreadMessagesOfUser(String username){
        return messagesDAO.getUnreadMessagesOfUser(username);
    }

    // Add message
    public Boolean addMessage(MessagesWS message) throws ParseException {
        Messages newMessage = new Messages(message);
        newMessage.setSender(userDAO.findByUsername(message.getSender()));
        newMessage.setReceiver(userDAO.findByUsername(message.getReceiver()));
        try {
            messagesDAO.addMessage(newMessage);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Update message
    public Boolean updateMessage(MessagesWS message, Long id) throws ParseException {
        Messages oldMessage = messagesDAO.getMessage(id);
        Messages updatedMessage = new Messages(message);
        updatedMessage.setId(oldMessage.getId());
        updatedMessage.setSender(oldMessage.getSender());
        updatedMessage.setReceiver(oldMessage.getReceiver());
        try {
            messagesDAO.updateMessage(updatedMessage);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get message by id
    public MessagesWS getMessage(Long id) {
        return new MessagesWS(messagesDAO.getMessage(id));
    }

    // Delete message
    public Boolean deleteMessage(Long id) {
        if(messagesDAO.getMessage(id) != null){
            return messagesDAO.deleteMessage(id);
        }

        return false;
    }

}
