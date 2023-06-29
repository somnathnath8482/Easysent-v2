package easysent.in.Room.Threads;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class Active_Thread {

    private int rid;
    @SerializedName("phone")
    private String phone;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_pic")
    private String profilePic;


    @ColumnInfo(name = "user_id")
    private String id;

    @SerializedName("fstatus")
    private String fstatus;

    @SerializedName("gender")
    private String gender;

    @SerializedName("about")
    private String about;

    @SerializedName("is_verifyed")
    private String isVerifyed;

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("unread")
    private int unread;

    @SerializedName("cash_pic")
    private String cash_pic;


    private String sender;
    private String thread_id;
    private String createAt;
    private String reciver;
    private String last_message;
    private String last_message_type;
    private String last_message_status;
    private String last_message_time;
    public Active_Thread() {
    }

    public Active_Thread(int rid, String phone, String name, String profilePic, String id, String fstatus, String gender, String about, String isVerifyed, String email, String token, String sender, String thread_id, String createAt, String reciver, String last_message, String last_message_type, String last_message_status, String last_message_time) {
        this.rid = rid;
        this.phone = phone;
        this.name = name;
        this.profilePic = profilePic;
        this.id = id;
        this.fstatus = fstatus;
        this.gender = gender;
        this.about = about;
        this.isVerifyed = isVerifyed;
        this.email = email;
        this.token = token;
        this.sender = sender;
        this.thread_id = thread_id;
        this.createAt = createAt;
        this.reciver = reciver;
        this.last_message = last_message;
        this.last_message_type = last_message_type;
        this.last_message_status = last_message_status;
        this.last_message_time = last_message_time;
    }

    public String getLast_message_time() {
        return last_message_time;
    }

    public void setLast_message_time(String last_message_time) {
        this.last_message_time = last_message_time;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getIsVerifyed() {
        return isVerifyed;
    }

    public void setIsVerifyed(String isVerifyed) {
        this.isVerifyed = isVerifyed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getLast_message_type() {
        return last_message_type;
    }

    public void setLast_message_type(String last_message_type) {
        this.last_message_type = last_message_type;
    }

    public String getLast_message_status() {
        return last_message_status;
    }

    public void setLast_message_status(String last_message_status) {
        this.last_message_status = last_message_status;
    }

    public String getCash_pic() {
        return cash_pic;
    }

    public void setCash_pic(String cash_pic) {
        this.cash_pic = cash_pic;
    }
}
