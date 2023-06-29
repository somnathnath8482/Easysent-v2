package easysent.in.Room.Groups;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(indices = {@Index(value = {"groupId"},unique = true)})
public class Groups {

    @PrimaryKey(autoGenerate = true)
    private int rid;

    @SerializedName("date_of_join")
    private String dateOfJoin;

    @SerializedName("creator")
    private String creator;

    @SerializedName("group_primary_id")
    private String groupPrimaryId;

    @SerializedName("user_count")
    private String userCount;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("group_id")
    private String groupId;

    @SerializedName("joind_by")
    private String joindBy;

    @SerializedName("group_user_id")
    private String groupUserId;

    @SerializedName("name")
    private String name;

    @SerializedName("group_desc")
    private String groupDesc;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("group_master_id")
    private String groupMasterId;
    @SerializedName("unread")
    private int unread;

    private String sender;
    private String createAt;
    private String last_message;
    private String last_message_type;
    private String last_message_status;
    private String last_message_time;

    @SerializedName("dp")
    private String dp;
    public Groups() {
    }

    public Groups(String dateOfJoin, String creator, String groupPrimaryId, String userCount, String userId, String groupId, String joindBy, String groupUserId, String name, String groupDesc, String createdAt, String groupMasterId, int unread, String dp) {
        this.dateOfJoin = dateOfJoin;
        this.creator = creator;
        this.groupPrimaryId = groupPrimaryId;
        this.userCount = userCount;
        this.userId = userId;
        this.groupId = groupId;
        this.joindBy = joindBy;
        this.groupUserId = groupUserId;
        this.name = name;
        this.groupDesc = groupDesc;
        this.createdAt = createdAt;
        this.groupMasterId = groupMasterId;
        this.unread = unread;
        this.dp = dp;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getGroupPrimaryId() {
        return groupPrimaryId;
    }

    public void setGroupPrimaryId(String groupPrimaryId) {
        this.groupPrimaryId = groupPrimaryId;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getJoindBy() {
        return joindBy;
    }

    public void setJoindBy(String joindBy) {
        this.joindBy = joindBy;
    }

    public String getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupMasterId() {
        return groupMasterId;
    }

    public void setGroupMasterId(String groupMasterId) {
        this.groupMasterId = groupMasterId;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
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

    public String getLast_message_time() {
        return last_message_time;
    }

    public void setLast_message_time(String last_message_time) {
        this.last_message_time = last_message_time;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
