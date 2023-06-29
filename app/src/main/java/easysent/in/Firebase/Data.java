package easysent.in.Firebase;

public class Data {
        String type;
        String thread;
        String message;
        String sender;

        String ip;

    public Data() {
    }

    public Data(String type, String thread, String message) {
        this.type = type;
        this.thread = thread;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Data{" +
                "type='" + type + '\'' +
                ", thread='" + thread + '\'' +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
