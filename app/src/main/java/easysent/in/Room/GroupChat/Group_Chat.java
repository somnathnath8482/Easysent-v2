package easysent.in.Room.GroupChat;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(indices = {@Index(value = {"id"},unique = true)})
public class Group_Chat {
    @PrimaryKey(autoGenerate = true)
    private int rid;

    @SerializedName("is_deleted")
    private String isDeleted;

    @SerializedName("attachment")
    private String attachment;

    @SerializedName("group_id")
    private String groupId;

    @SerializedName("sender")
    private String sender;

    @SerializedName("attachment_title")
    private String attachmentTitle;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private String id;

    @SerializedName("replay_of")
    private String replayOf;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("seen")
    private int seen;

    public Group_Chat() {
    }

    public Group_Chat(String isDeleted, String attachment, String groupId, String sender, String attachmentTitle, String createdAt, String id, String replayOf, String message, String type, int seen) {
        this.isDeleted = isDeleted;
        this.attachment = attachment;
        this.groupId = groupId;
        this.sender = sender;
        this.attachmentTitle = attachmentTitle;
        this.createdAt = createdAt;
        this.id = id;
        this.replayOf = replayOf;
        this.message = message;
        this.type = type;
        this.seen = seen;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String  attachment) {
        this.attachment = attachment;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAttachmentTitle() {
        return attachmentTitle;
    }

    public void setAttachmentTitle(String attachmentTitle) {
        this.attachmentTitle = attachmentTitle;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplayOf() {
        return replayOf;
    }

    public void setReplayOf(String replayOf) {
        this.replayOf = replayOf;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "Group_Chat{" +
                "rid=" + rid +
                ", isDeleted='" + isDeleted + '\'' +
                ", attachment='" + attachment + '\'' +
                ", groupId='" + groupId + '\'' +
                ", sender='" + sender + '\'' +
                ", attachmentTitle='" + attachmentTitle + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", id='" + id + '\'' +
                ", replayOf='" + replayOf + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", seen=" + seen +
                '}';
    }
}
