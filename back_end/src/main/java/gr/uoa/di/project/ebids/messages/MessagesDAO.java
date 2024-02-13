package gr.uoa.di.project.ebids.messages;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

/* * * * * * * * * * * * * * * * *
 * SQL queries for messages entity
 * * * * * * * * * * * * * * * * */

@Repository
public class MessagesDAO {
    @PersistenceContext
    private EntityManager entityManager;

    // Select message by id
    @Transactional
    public Messages getMessage(Long id){
        return entityManager.find(Messages.class, id);
    }

    // Select all messages by page
    @Transactional
    public ArrayList<Messages> getAllMessages(Integer page, Integer limit){
        try {
            return (ArrayList<Messages>) entityManager.createQuery("select m from Messages m", Messages.class)
                                        .setFirstResult((page - 1) * limit)
                                        .setMaxResults(limit)
                                        .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Get number of all messages
    @Transactional
    public Long getAllMessagesCount(){
        try {
            return entityManager.createQuery("select count (*) from Messages m", Long.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Select unread messages of user with username = username
    @Transactional
    public Long getUnreadMessagesOfUser(String username){
        try {
            return entityManager.createQuery("select count (*) from Messages m where m.receiver.username like '%" + username + "%' and m.opened = false", Long.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Select messages filtered
    @Transactional
    public ArrayList<Messages> getMessages(Map<String, String> parameters, String username, Boolean send){
        // Create sql query using the parameters in the url
        String query = "select m from Messages m where ";
        if(send){
            query += "m.sender.username like '%" + username + "%' and ";
        } else {
            query += "m.receiver.username like '%" + username + "%' and ";
        }

        Integer page = null;
        Integer limit = null;
        String orderby = null;
        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("title")){
                    query += "m.title like '%" + value + "%' and ";
                } else if(key.equals("page")){
                    page = Integer.parseInt(value);
                } else if(key.equals("limit")){
                    limit = Integer.parseInt(value);
                } else if(key.equals("orderby")){
                    orderby = value;
                }
            }
            query += "1 = 1";
            if(page == null){
                page = 1;
            }
            if(limit == null){
                limit = 10;
            }
            if(orderby == null){
                query += " order by m.id";
            } else {
                if(orderby.startsWith("-")){
                    query += " order by m." + orderby.substring(1) + " desc";
                } else {
                    query += " order by m." + orderby + " asc";
                }
            }

            return (ArrayList<Messages>) entityManager.createQuery(query, Messages.class)
                                        .setFirstResult((page - 1) * limit)
                                        .setMaxResults(limit)
                                        .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Get number of messages filtered
    @Transactional
    public Long getMessagesCount(Map<String, String> parameters, String username, Boolean send){
        String query = "select count (*) from Messages m where ";
        if(send){
            query += "m.sender.username like '%" + username + "%' and ";
        } else {
            query += "m.receiver.username like '%" + username + "%' and ";
        }
        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("title")){
                    query += "m.title like '%" + value + "%' and ";
                }
            }
            query += "1 = 1";

            return entityManager.createQuery(query, Long.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Boolean addMessage(Messages message) {
        try {
            entityManager.persist(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean updateMessage(Messages message) {
        try {
            entityManager.merge(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteMessage(Long id) {
        try {
            entityManager.createQuery("delete from Messages m where m.id =: id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
