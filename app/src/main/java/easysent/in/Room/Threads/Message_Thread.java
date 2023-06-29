package easysent.in.Room.Threads;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"thread_id"},unique = true)})
public class Message_Thread {

    @PrimaryKey(autoGenerate = true)
    private int rid;

    private String sender;

@ColumnInfo(name = "thread_id")
    private String id;
    private String createAt;
    private String reciver;
    private int unread;
    private String last_message;
    private String last_message_type;
    private String last_message_status;
    private String last_message_time;

    public Message_Thread() {
    }

    public Message_Thread(String sender, String id, String createAt, String reciver, String last_message, String last_message_type, String last_message_status, String last_message_time) {
        this.sender = sender;
        this.id = id;
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

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
