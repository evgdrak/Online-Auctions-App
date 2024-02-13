package gr.uoa.di.project.ebids.bids;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/* * * * * * * * * * * * * * *
 * SQL queries for bids entity
 * * * * * * * * * * * * * * */

@Repository
public class BidsDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Bids> getBids(){
        return entityManager.createQuery("select i from Bids i", Bids.class).getResultList();
    }

    // select bid by id
    @Transactional
    public Bids getBid(Long id){
        return entityManager.find(Bids.class, id);
    }

    @Transactional
    public Boolean updateBid(Bids bid) {
        try {
            entityManager.merge(bid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean addBid(Bids bid) {
        try {
            entityManager.persist(bid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteBid(Long id) {
        try {
            entityManager.createQuery("delete from Bids i where i.id =: id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
