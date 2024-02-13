package gr.uoa.di.project.ebids.user;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/* * * * * * * * * * * * * * *
 * SQL queries for user entity
 * * * * * * * * * * * * * * */

@Repository
public class UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    // Select all users from newest to oldest and not admin
    public List<User> findUsers(){
        return entityManager.createQuery("select u from User u where u.role <> 'Admin' order by u.id desc", User.class).getResultList();
    }

    // Select user by username
    @Transactional
    public User findByUsername(String username){
        try {
            return entityManager.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Boolean updateUser(User user) {
        try {
            entityManager.merge(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean createUser(User user) {
        try {
            entityManager.persist(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
