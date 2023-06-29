package easysent.in.Room.Block;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity
public class Block {
    @PrimaryKey(autoGenerate = true)
    private int rid;
    @SerializedName("to_user")
    private String toUser;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private String id;

    @SerializedName("thread")
    private String thread;

    @SerializedName("from_user")
    private String fromUser;

    public Block(String toUser, String id, String thread, String fromUser) {
        this.toUser = toUser;
        this.id = id;
        this.thread = thread;
        this.fromUser = fromUser;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
