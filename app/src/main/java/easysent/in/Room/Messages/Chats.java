package easysent.in.Room.Messages;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(indices = {@Index(value = {"chat_id"},unique = true)})
public class Chats {

    @PrimaryKey(autoGenerate = true)
    private int rid;

    @SerializedName("attachment")
    private String attachment;

    @SerializedName("sender")
    private String sender;

    @SerializedName("created_at")
    private String createdAt;

  @ColumnInfo(name = "chat_id")
    private String id;

    @SerializedName("thread")
    private String thread;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("replay_of")
    private String replay_of;



    @SerializedName("reciver")
    private String reciver;

    @SerializedName("seen")
    private String seen;
    @SerializedName("is_deleted")
    private String is_deleted;
    public Chats() {
    }

    public Chats(String attachment, String sender, String createdAt, String id,
                 String thread, String message, String type, String reciver,
                 String seen,
                 String replay_of,
                 String is_deleted) {
        this.attachment = attachment;
        this.sender = sender;
        this.createdAt = createdAt;
        this.id = id;
        this.thread = thread;
        this.message = message;
        this.type = type;
        this.reciver = reciver;
        this.seen = seen;
        this.replay_of  =replay_of;
        this.is_deleted = is_deleted;

    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
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

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getReplay_of() {
        return replay_of;
    }

    public void setReplay_of(String replay_of) {
        this.replay_of = replay_of;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}
