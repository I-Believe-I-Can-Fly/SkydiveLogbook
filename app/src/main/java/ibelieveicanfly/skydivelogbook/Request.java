package ibelieveicanfly.skydivelogbook;

public class Request {
    private String UserID;
    private String Signer;
    private String JumpNr;
    private String UserName;

    public Request(){ }

    public Request(String UserID, String SignerID, String JumpNr, String userName){
        this.UserID = UserID;
        this.Signer = SignerID;
        this.JumpNr = JumpNr;
        this.UserName = userName;
    }

    public String getSigner() {
        return Signer;
    }

    public String getUserID() {
        return UserID;
    }

    public String getJumpNr() {
        return JumpNr;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setSigner(String signer) {
        Signer = signer;
    }

    public void setJumpNr(String jumpNr) {
        JumpNr = jumpNr;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserName() {
        return UserName;
    }
}
