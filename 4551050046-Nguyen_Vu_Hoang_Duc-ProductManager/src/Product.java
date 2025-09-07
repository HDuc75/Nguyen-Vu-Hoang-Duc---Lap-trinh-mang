// Product.java
public class Product {
    private int id;
    private String name;
    private String category;
    private String description;
    private double price; // giá gốc cho 1 đơn vị
    private int quantity; // số lượng tồn kho
    private double discountPercent; // phần trăm giảm giá (0-100)

    public Product(int id, String name, String category, String description,
                   double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.discountPercent = 0.0;
    }

    // Getters và Setters cơ bản
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getDiscountPercent() { return discountPercent; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setDiscountPercent(double discountPercent) {
        if (discountPercent < 0) discountPercent = 0;
        if (discountPercent > 100) discountPercent = 100;
        this.discountPercent = discountPercent;
    }

    // Giá sau khi áp dụng giảm giá
    public double getPriceAfterDiscount() {
        return roundTwoDecimals(price * (1 - discountPercent / 100.0));
    }

    private double roundTwoDecimals(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return String.format("ID: %d\n%s\nDanh mục: %s\nGiá gốc: %.2f\nGiảm: %.1f%%\nGiá bán: %.2f\nSố lượng: %d\nMô tả: %s\n\n",
                id, name, category, price, discountPercent, getPriceAfterDiscount(), quantity, description);
    }
}
