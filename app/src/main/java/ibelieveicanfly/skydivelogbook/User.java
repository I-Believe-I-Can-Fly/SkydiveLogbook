package ibelieveicanfly.skydivelogbook;

public class User {
    public String firstName;
    public String lastName;
    public String licence;
    public String certificate;
    public String email;
    public String dateOfBirth;
    public String userID;
    public String dropZone;
    public String plane;
    public String equipment;
    public String exitAlt;
    public String freefall;
    public String canopyAlt;


    public User() {

    }

    public User(String firstName, String lastName, String dateOfBirth, String license, String certificate, String email, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.licence = license;
        this.certificate = certificate;
        this.email = email;
        this.userID = userID;
        this.dropZone = null;
        this.plane = null;
        this.equipment = null;
        this.exitAlt = null;
        this.freefall = null;
        this.canopyAlt = null;
    }

    public void updatePrefs(String dropZone, String plane, String equipment, String exitAlt, String freefall, String canopyAlt){
        this.dropZone = dropZone;
        this.plane = plane;
        this.equipment = equipment;
        this.exitAlt = exitAlt;
        this.freefall = freefall;
        this.canopyAlt = canopyAlt;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
