package soumyadeb.foodsinghadmin.models;

/**
 * Created by Soumya Deb on 13-09-2017.
 */

public class Category {
    private String category, image;

    public Category(String category, String image) {
        this.category = category;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
