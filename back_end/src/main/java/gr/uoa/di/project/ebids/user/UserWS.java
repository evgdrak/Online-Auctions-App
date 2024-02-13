package gr.uoa.di.project.ebids.user;

/* * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Class for response that needs to have user details
 * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UserWS {
    private long id;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String TIN;
    private Role role;
    private boolean isVerified = false;

    public UserWS(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.country = user.getCountry();
        this.TIN = user.getTIN();
        this.role = user.getRole();
        this.isVerified = user.isVerified();
    }

    public UserWS() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
