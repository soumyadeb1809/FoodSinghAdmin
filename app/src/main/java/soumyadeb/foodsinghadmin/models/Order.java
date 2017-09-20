package soumyadeb.foodsinghadmin.models;

/**
 * Created by Soumya Deb on 13-09-2017.
 */

public class Order {
    private String id, timestamp, item, amount, mobile, address, comments;

    public Order(String id, String timestamp, String item, String amount, String mobile, String address, String comments) {
        this.id = id;
        this.timestamp = timestamp;
        this.item = item;
        this.amount = amount;
        this.mobile = mobile;
        this.address = address;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
