package gr.uoa.di.project.ebids.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* * * * * * * * * * * * * * * * * * * * * * * * *
 * Controller for requests on items and categories
 * * * * * * * * * * * * * * * * * * * * * * * * */

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    // Get all items
    @GetMapping(value = "/items", produces = "application/json")
    public ResponseEntity<ItemList> getItemsFiltered(@RequestParam(required = false) Map<String, String> parameters) {
        return new ResponseEntity<>(itemService.getItemsFiltered(parameters), HttpStatus.OK);
    }

    // Get items that user put a bid on
    @GetMapping(value = "/items/bids/{id}", produces = "application/json")
    public ResponseEntity<List<ItemWS>> getItemsWithBidsFromUser(@PathVariable String id) {
        return new ResponseEntity<>(itemService.getItemsWithBidsFromUser(id), HttpStatus.OK);
    }

    // Add new item
    @PostMapping(value = "/items", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Boolean> addItem(@RequestPart("item") ItemWS item,
                                           @RequestPart("photo") MultipartFile[] file ) throws ParseException {

        try {
            Set<Photos> photos = uploadPhoto(file);
            Set<PhotosWS> ph = new HashSet<>();
            for(Photos p : photos){
                PhotosWS photo = new PhotosWS(p);
                ph.add(photo);
            }
            item.setPhotos(ph);
            return new ResponseEntity<>(itemService.addItem(item), HttpStatus.OK);

        } catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get all photos
    public Set<Photos> uploadPhoto(MultipartFile[] multipartFiles) throws IOException {
        Set<Photos> photos = new HashSet<>();
        for (MultipartFile file: multipartFiles){
            Photos photo = new Photos(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );

            photos.add(photo);
        }

        return photos;
    }

    // Get item by id
    @GetMapping(value = "/items/{id}", produces = "application/json")
    public ResponseEntity<ItemWS> getItem(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItem(id), HttpStatus.OK);
    }

    // Update item
    @PutMapping(value = "/items/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Boolean> updateItem(@RequestPart("item") ItemWS item, @PathVariable Long id, @RequestPart("photo") MultipartFile[] file ) throws ParseException {

        try {
            Set<Photos> photos = uploadPhoto(file);

            for(Photos p : photos){
                PhotosWS photo = new PhotosWS(p);
                item.getPhotos().add(photo);
            }

            return new ResponseEntity<>(itemService.updateItem(item, id), HttpStatus.OK);

        } catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Delete item
    @DeleteMapping(value = "/items/{id}", produces = "application/json")
    public ResponseEntity<Boolean> deleteItem(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.deleteItem(id), HttpStatus.OK);
    }

    /*@GetMapping(value = "/categories", produces = "application/json")
    public ResponseEntity<List<CategoryWS>> getCategories() {
        return new ResponseEntity<>(itemService.getCategories(), HttpStatus.OK);
    }

    @PostMapping(value = "/categories", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Boolean> addCategory(@RequestBody CategoryWS item) {
        return new ResponseEntity<>(itemService.addCategory(item), HttpStatus.OK);
    }

    @GetMapping(value = "/categories/{id}", produces = "application/json")
    public ResponseEntity<CategoryWS> getCategory(@PathVariable String id) {
        return new ResponseEntity<>(itemService.getCategory(id), HttpStatus.OK);
    }

    @PutMapping(value = "/categories/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> updateCategory(@RequestBody CategoryWS item, @PathVariable String id) {
        return new ResponseEntity<>(itemService.updateCategory(item, id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/categories/{id}", produces = "application/json")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable String id) {
        return new ResponseEntity<>(itemService.deleteCategory(id), HttpStatus.OK);
    }*/

}
