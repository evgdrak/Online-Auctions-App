package gr.uoa.di.project.ebids.item;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/* * * * * * * *
 * Photos entity
 * * * * * * * */

@Entity
@Table(name = "photos")
public class Photos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photoid", nullable = false)
    private Long id;

    @Column(name = "image", length = 500000)
    private byte[] image;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "photo", cascade = CascadeType.ALL)
    private Set<ItemPhotos> item_photos = new HashSet<>();

    public Photos(){}

    public Photos(String name, String type, byte[] image) {
        this.name = name;
        this.image = image;
        this.type = type;
    }

    public Photos(PhotosWS photo) {
        this.id = photo.getId();
        this.name = photo.getName();
        this.image = photo.getImage();
        this.type = photo.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long photoId) {
        this.id = photoId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<ItemPhotos> getItem_photos() {
        return item_photos;
    }

    public void setItem_photos(Set<ItemPhotos> item_photos) {
        this.item_photos = item_photos;
    }
}
