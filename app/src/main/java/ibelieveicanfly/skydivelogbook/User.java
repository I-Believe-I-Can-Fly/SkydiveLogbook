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

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLicence() {
        return licence;
    }
}
