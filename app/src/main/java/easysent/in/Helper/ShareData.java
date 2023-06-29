package easysent.in.Helper;

/**
 * Created by Somnath nath on 12,June,2023
 * Artix Development,
 * India.
 */
public class ShareData {
    String type,message,file;

    public ShareData() {
    }

    public ShareData(String type, String message, String file) {
        this.type = type;
        this.message = message;
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
