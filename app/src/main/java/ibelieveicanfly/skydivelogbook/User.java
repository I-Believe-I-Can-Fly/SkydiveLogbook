package ibelieveicanfly.skydivelogbook;

public class User {
    public String firstName;
    public String lastName;
    public String licence;
    public String certificate;
    public String email;
    public String dateOfBirth;


    public User() {

    }

    public User(String firstName, String lastName, String dateOfBirth, String license, String certificate, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.licence = license;
        this.certificate = certificate;
        this.email = email;
    }
}
