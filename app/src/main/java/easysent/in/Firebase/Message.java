package easysent.in.Firebase;


public class Message {
    public Data data;
    public  String name;

    public Message(Data data, String name) {
        this.data = data;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message{" +
                "data=" + data +
                ", name='" + name + '\'' +
                '}';
    }
}
