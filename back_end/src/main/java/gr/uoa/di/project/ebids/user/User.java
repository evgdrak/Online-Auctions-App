package gr.uoa.di.project.ebids.user;

import gr.uoa.di.project.ebids.authentication.RegisterUser;
import gr.uoa.di.project.ebids.bids.Bids;
import gr.uoa.di.project.ebids.item.Item;
import gr.uoa.di.project.ebids.messages.Messages;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import javax.persistence.*;

/* * * * * * * *
 * User entity
 * * * * * * * */

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @Column(name = "userid", nullable = false, unique = true)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "tin", nullable = false)
    private String TIN;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "isverified")
    private boolean isVerified;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Item> items = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Bids> bids = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<Messages> messages_s = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    private Set<Messages> messages_r = new HashSet<>();

    public User(){}

    /* User to insert to database */
    public User(UserWS userWS) {
        this.id = userWS.getId();
        this.username = userWS.getUsername();
        this.lastName = userWS.getLastName();
        this.firstName = userWS.getFirstName();
        this.email = userWS.getEmail();
        this.phone = userWS.getPhone();
        this.address = userWS.getAddress();
        this.city = userWS.getCity();
        this.country = userWS.getCountry();
        this.TIN = userWS.getTIN();
        this.role = userWS.getRole();
        this.isVerified = userWS.isVerified();
    }

    public User(RegisterUser registerUser) {
        this.username = registerUser.getUsername();
        this.password = registerUser.getPassword();
        this.lastName = registerUser.getLastName();
        this.firstName = registerUser.getFirstName();
        this.email = registerUser.getEmail();
        this.phone = registerUser.getPhone();
        this.address = registerUser.getAddress();
        this.city = registerUser.getCity();
        this.country = registerUser.getCountry();
        this.TIN = registerUser.getTIN();
        this.role = registerUser.getRole();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isVerified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        //if(isVerified) {
            if (this.role == Role.Admin) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        //}
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Messages> getMessages_s() {
        return messages_s;
    }

    public void setMessages_s(Set<Messages> messages_s) {
        this.messages_s = messages_s;
    }

    public Set<Messages> getMessages_r() {
        return messages_r;
    }

    public void setMessages_r(Set<Messages> messages_r) {
        this.messages_r = messages_r;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Bids> getBids() {
        return bids;
    }

    public void setBids(Set<Bids> bids) {
        this.bids = bids;
    }

    public Boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

}
