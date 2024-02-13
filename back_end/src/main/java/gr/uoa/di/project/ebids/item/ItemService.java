package gr.uoa.di.project.ebids.item;

import gr.uoa.di.project.ebids.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/* * * * * * * * * * * * * * * * * * * * * * * * * *
 * Functionalities for items, photos and categories
 * * * * * * * * * * * * * * * * * * * * * * * * * */

@Service
public class ItemService {
    @Autowired
    ItemDAO itemDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    private ItemRepository itemRepository;

    public List<ItemWS> getItems(){
        List<ItemWS> itemWS = new ArrayList<>();
        List<Item> items = itemDAO.getItems();
        for(Item item: items){
            itemWS.add(new ItemWS(item));
        }
        return itemWS;
    }

    public ItemList getItemsFiltered(Map<String, String> parameters){
        ItemList itemList = new ItemList();
        List<ItemWS> items = new ArrayList<>();

        itemList.setCount(itemDAO.getItemsFilteredCount(parameters));

        for(Item item: itemDAO.getItemsFiltered(parameters)){
            items.add(new ItemWS(item));
        }

        itemList.setResults(items);

        return itemList;
    }

    public List<ItemWS> getItemsWithBidsFromUser(String username){
        List<ItemWS> itemWS = new ArrayList<>();
        List<Item> items = itemDAO.getItemsWithBidsFromUser(username);
        for(Item item: items){
            itemWS.add(new ItemWS(item));
        }
        return itemWS;
    }

    public Boolean addItem(ItemWS item) throws ParseException {
        Item newItem = new Item(item);
        newItem.setUser(userDAO.findByUsername(item.getUserID()));
        try {
            itemDAO.addItem(newItem);

            // add categories to item
            for(CategoryWS categoryWS : item.getCategories()){
                Category category = itemDAO.getCategory(categoryWS.getName());
                Item_Category item_category = new Item_Category(new Item_Category_Key(newItem.getId(), category.getId()));
                item_category.setCategory(category);
                item_category.setItem(newItem);
                itemDAO.addItemCategory(item_category);
            }

            // add photos to item
            for(PhotosWS photosWS : item.getPhotos()){
                Photos photo = new Photos(photosWS);
                itemDAO.addPhoto(photo);
                ItemPhotos item_photo = new ItemPhotos(new ItemPhotosPK(newItem.getId(), photo.getId()));
                item_photo.setPhoto(photo);
                item_photo.setItem(newItem);
                itemDAO.addItemPhoto(item_photo);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ItemWS getItem(Long id) {
        return new ItemWS(itemDAO.getItem(id));
    }

    public Boolean updateItem(ItemWS itemWS, Long id) throws ParseException {
        Item item = itemDAO.getItem(id);
        if(item != null){
            // change something if it has a different value
            if(item.getLocation() != itemWS.getLocation()){
                item.setLocation(itemWS.getLocation());
            }
            if(item.getLatitude() != itemWS.getLatitude()){
                item.setLatitude(itemWS.getLatitude());
            }
            if(item.getLongitude() != itemWS.getLongitude()){
                item.setLongitude(itemWS.getLongitude());
            }
            if(item.getCountry() != itemWS.getCountry()){
                item.setCountry(itemWS.getCountry());
            }
            if(item.getBuy_price() != itemWS.getBuy_price()){
                item.setBuy_price(itemWS.getBuy_price());
            }
            if(item.getFirst_bid() != itemWS.getFirst_bid()){
                item.setFirst_bid(itemWS.getFirst_bid());
            }
            if(item.getEnds() != (new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").parse(itemWS.getEnds()))){
                item.setEnds(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").parse(itemWS.getEnds()));
            }
            if(item.getDescription() != itemWS.getDescription()){
                item.setDescription(itemWS.getDescription());
            }

            itemDAO.updateItem(item);

            itemDAO.deleteItemCategories(item.getId());
            for(CategoryWS categoryWS : itemWS.getCategories()){
                Category category = itemDAO.getCategory(categoryWS.getName());
                Item_Category item_category = new Item_Category(new Item_Category_Key(item.getId(), category.getId()));
                item_category.setCategory(category);
                item_category.setItem(item);
                itemDAO.addItemCategory(item_category);
            }

            // current photo ids
            ArrayList<Long> list = new ArrayList<>();
            for(ItemPhotos item_photo: item.getItem_photos()){
                list.add(item_photo.getId().photoID);
            }

            // delete relation between photos and items
            // so that we can delete a photo after if user removed it
            itemDAO.deleteItemPhotos(item.getId());
            for(PhotosWS photosWS : itemWS.getPhotos()){
                Photos photo = new Photos(photosWS);
                // photo does not exist
                if(photo.getType() != null) {
                    itemDAO.addPhoto(photo);
                    ItemPhotos item_photo = new ItemPhotos(new ItemPhotosPK(item.getId(), photo.getId()));
                    item_photo.setPhoto(photo);
                    item_photo.setItem(item);
                    itemDAO.addItemPhoto(item_photo);
                // photo already exists
                } else if(photo.getId() != null) {
                    photo = itemDAO.getPhoto(photo.getId());
                    list.remove(photo.getId());
                    ItemPhotos item_photo = new ItemPhotos(new ItemPhotosPK(item.getId(), photo.getId()));
                    item_photo.setPhoto(photo);
                    item_photo.setItem(item);
                    itemDAO.addItemPhoto(item_photo);
                }
            }

            // delete removed photos
            for(Long num: list){
                itemDAO.deletePhoto(num);
            }

            return true;
        }

        return false;
    }

    public Boolean deleteItem(Long id) {
        Item item = itemDAO.getItem(id);
        if(item != null){
            ArrayList<Long> list = new ArrayList<>();
            for(ItemPhotos itemPhotos: item.getItem_photos()){
                list.add(itemPhotos.getPhoto().getId());
            }
            itemDAO.deleteItemCategory(id);
            itemDAO.deleteItemPhoto(id);
            for(Long num: list){
                itemDAO.deletePhoto(num);
            }
            return itemDAO.deleteItem(id);
        }

        return false;
    }

    public List<CategoryWS> getCategories(){
        List<CategoryWS> categoryWS = new ArrayList<>();
        List<Category> categories = itemDAO.getCategories();
        for(Category category: categories){
            categoryWS.add(new CategoryWS(category));
        }
        return categoryWS;
    }

    public Boolean addCategory(CategoryWS category) {
        Category newCategory = new Category(category);
        return itemDAO.addCategory(newCategory);
    }

    public CategoryWS getCategory(String name) {
        return new CategoryWS(itemDAO.getCategory(name));
    }

    public Boolean updateCategory(CategoryWS newCategory, String name) {
        Category category = itemDAO.getCategory(name);
        if(category != null){
            category.setName(newCategory.getName());
            return itemDAO.updateCategory(category);
        }

        return false;
    }

    public Boolean deleteCategory(String name) {
        if(itemDAO.getCategory(name) != null){
            return itemDAO.deleteCategory(name);
        }

        return false;
    }


////////////////////////////////////



    public List<PhotosWS> getPhotos(){
        List<PhotosWS> photosWS = new ArrayList<>();
        List<Photos> photos = itemDAO.getPhotos();
        for(Photos photo: photos){
            photosWS.add(new PhotosWS(photo));
        }
        return photosWS;
    }

    public Boolean addPhoto(PhotosWS photo) {
        Photos newPhoto = new Photos(photo);
        return itemDAO.addPhoto(newPhoto);
    }

    public PhotosWS getPhoto(Long id) {
        return new PhotosWS(itemDAO.getPhoto(id));
    }

    public Boolean updatePhoto(PhotosWS newPhoto, Long id) {
        Photos photo = itemDAO.getPhoto(id);
        if(photo != null){
            photo.setName(newPhoto.getName());
            return itemDAO.updatePhoto(photo);
        }

        return false;
    }

    public Boolean deletePhoto(Long id) {
        if(itemDAO.getPhoto(id) != null){
            return itemDAO.deletePhoto(id);
        }

        return false;
    }

}
