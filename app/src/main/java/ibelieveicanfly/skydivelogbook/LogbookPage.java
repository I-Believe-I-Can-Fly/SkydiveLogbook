package ibelieveicanfly.skydivelogbook;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LogbookPage {

    public Integer jumpNr;
    public String date;
    public String dz;
    public String plane;
    public String equipment;
    public String exit;
    public String freefall;
    public String canopy;
    public String comments;
    public String signature;
    public boolean approved;

    public LogbookPage() {

    }

    public LogbookPage(Integer jumpNr,
                       String date,
                       String dz,
                       String plane,
                       String equipment,
                       String exit,
                       String freefall,
                       String canopy,
                       String comments,
                       String signature,
                       boolean approved) {
        this.jumpNr = jumpNr;
        this.date = date;
        this.dz = dz;
        this.plane = plane;
        this.equipment = equipment;
        this.exit = exit;
        this.freefall = freefall;
        this.canopy = canopy;
        this.comments = comments;
        this.signature = signature;
        this.approved = false;
    }

    public Integer getJumpNr() {
        return jumpNr;
    }

    public String getDate() {
        return date;
    }

    public String getDz() {
        return dz;
    }

    public String getPlane() {
        return plane;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getCanopy() {
        return canopy;
    }

    public String getComments() {
        return comments;
    }

    public String getExit() {
        return exit;
    }

    public String getFreefall() {
        return freefall;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setJumpNr(Integer jumpNr) {
        this.jumpNr = jumpNr;
    }

    public void setCanopy(String canopy) {
        this.canopy = canopy;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public void setFreefall(String freefall) {
        this.freefall = freefall;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
