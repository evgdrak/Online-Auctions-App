package gr.uoa.di.project.ebids.item;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* * * * * * * * * * * * * * * * * * *
 * SQL queries for items and categories
 * * * * * * * * * * * * * * * * * * */

@Repository
public class ItemDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Item> getItems(){
        return entityManager.createQuery("select i from Item i", Item.class).getResultList();
    }

    @Transactional
    public List<Item> getItemsWithBidsFromUser(String username){
        return entityManager.createQuery("select distinct i from Item i, Bids b where b.user.username =: username and b.item.id = i.id", Item.class).setParameter("username", username).getResultList();
    }

    // Select item by id
    @Transactional
    public Item getItem(Long id){
        return entityManager.find(Item.class, id);
    }

    // Select items filtered
    @Transactional
    public ArrayList<Item> getItemsFiltered(Map<String, String> parameters){
        // Create sql query using the parameters in the url
        String query = "select distinct (i) from Item i, Item_Category ic, Category c where ";
        Integer page = null;
        Integer limit = null;
        String orderby = null;
        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("category")){
                    query += "ic.id.itemID = i.id and ic.id.categoryID = c.id and c.name like '%" + value + "%' and ";
                } else if(key.equals("description")){
                    query += "i.description like '%" + value + "%' and ";
                } else if(key.equals("location")){
                    query += "i.location like '%" + value + "%' and ";
                } else if(key.equals("value_min")){
                    query += "i.currently >= " + value + " and ";
                } else if(key.equals("value_max")){
                    query += "i.currently <= " + value + " and ";
                } else if(key.equals("ends_max")){
                    query += "i.ends <= '" + value + "' and ";
                } else if(key.equals("ends_min")){
                    query += "i.ends >= '" + value + "' and ";
                } else if(key.equals("page")){
                    page = Integer.parseInt(value);
                } else if(key.equals("limit")){
                    limit = Integer.parseInt(value);
                } else if(key.equals("orderby")){
                    orderby = value;
                } else if(key.equals("user")){
                    query += "i.user.username = '" + value + "' and ";
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
                query += " order by i.id";
            } else {
                if(orderby.startsWith("-")){
                    query += " order by i." + orderby.substring(1) + " desc";
                } else {
                    query += " order by i." + orderby + " asc";
                }
            }

            return (ArrayList<Item>) entityManager.createQuery(query, Item.class).setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    // Get number of items filtered
    @Transactional
    public Long getItemsFilteredCount(Map<String, String> parameters){
        String query = "select count(distinct (i)) from Item i, Item_Category ic, Category c where ";
        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("category")){
                    query += "ic.id.itemID = i.id and ic.id.categoryID = c.id and c.name like '%" + value + "%' and ";
                } else if(key.equals("description")){
                    query += "i.description like '%" + value + "%' and ";
                } else if(key.equals("location")){
                    query += "i.location like '%" + value + "%' and ";
                } else if(key.equals("value_min")){
                    query += "i.currently >= " + value + " and ";
                } else if(key.equals("value_max")){
                    query += "i.currently <= " + value + " and ";
                } else if(key.equals("ends_max")){
                    query += "i.ends <= '" + value + "' and ";
                } else if(key.equals("ends_min")){
                    query += "i.ends >= '" + value + "' and ";
                } else if(key.equals("user")){
                    query += "i.user.username = '" + value + "' and ";
                }
            }
            query += "1 = 1";

            return entityManager.createQuery(query, Long.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Boolean updateItem(Item item) {
        try {
            entityManager.merge(item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean addItem(Item item) {
        try {
            entityManager.persist(item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean addItemCategory(Item_Category item_category) {
        try {
            entityManager.merge(item_category);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteItemCategory(Long id) {
        try {
            entityManager.createQuery("delete from Item_Category ic where ic.item.id =: id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteItem(Long id) {
        try {
            entityManager.createQuery("delete from Item i where i.id =: id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public List<Category> getCategories(){
        return entityManager.createQuery("select c from Category c", Category.class).getResultList();
    }

    @Transactional
    public Category getCategory(String name){
        return entityManager.createQuery("select c from Category c where c.name = :name", Category.class).setParameter("name", name).getSingleResult();
    }

    @Transactional
    public Boolean updateCategory(Category category) {
        try {
            entityManager.merge(category);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean addCategory(Category category) {
        try {
            entityManager.persist(category);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteCategory(String name) {
        try {
            entityManager.createQuery("delete from Category c where c.name = :name").setParameter("name", name).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteItemCategories(Long id) {
        try {
            entityManager.createQuery("delete from Item_Category ic where ic.id.itemID = :id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

/////////////////////////
    @Transactional
    public Boolean addItemPhoto(ItemPhotos item_photo) {
        try {
            entityManager.merge(item_photo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteItemPhoto(Long id) {
        try {
            entityManager.createQuery("delete from ItemPhotos ip where ip.item.id =: id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public List<Photos> getPhotos(){
        return entityManager.createQuery("select p from Photos p", Photos.class).getResultList();
    }

    @Transactional
    public Photos getPhoto(Long id){
        return entityManager.createQuery("select p from Photos p where p.id = :id", Photos.class).setParameter("id", id).getSingleResult();
    }

    @Transactional
    public Boolean updatePhoto(Photos photo) {
        try {
            entityManager.merge(photo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean addPhoto(Photos photo) {
        try {
            entityManager.persist(photo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deletePhoto(Long id) {
        try {
            entityManager.createQuery("delete from Photos p where p.id = :id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteItemPhotos(Long id) {
        try {
            entityManager.createQuery("delete from ItemPhotos ip where ip.id.itemID = :id").setParameter("id", id).executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
