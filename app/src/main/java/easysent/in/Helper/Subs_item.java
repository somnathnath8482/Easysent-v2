package easysent.in.Helper;

public class Subs_item {
    String title,price,duration;

    public Subs_item(String title, String price, String duration) {
        this.title = title;
        this.price = price;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
