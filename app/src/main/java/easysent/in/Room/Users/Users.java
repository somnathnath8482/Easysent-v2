package easysent.in.Room.Users;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(indices = {@Index(value = {"email"},unique = true)})
public class Users {
    @PrimaryKey(autoGenerate = true)
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

    @SerializedName("ip")
    private String ip;

    @SerializedName("token")
    private String token;

    @SerializedName("cash_pic")
    private String cash_pic;

    public Users() {
    }

    public Users(String phone, String name, String profilePic, String id, String fstatus, String gender, String about, String isVerifyed, String email, String token) {
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
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
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

    public String getCash_pic() {
        return cash_pic;
    }

    public void setCash_pic(String cash_pic) {
        this.cash_pic = cash_pic;
    }
}
