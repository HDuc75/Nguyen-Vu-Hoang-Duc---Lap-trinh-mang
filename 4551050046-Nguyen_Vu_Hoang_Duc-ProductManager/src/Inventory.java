// Inventory.java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private ArrayList<Product> products = new ArrayList<>();
    private int nextId = 1;

    // Thêm sản phẩm (trả về id mới)
    public int addProduct(String name, String category, String description, double price, int quantity) {
        Product p = new Product(nextId++, name, category, description, price, quantity);
        products.add(p);
        return p.getId();
    }

    // Tìm sản phẩm theo id
    public Product findById(int id) {
        for (Product p : products) if (p.getId() == id) return p;
        return null;
    }

    // Cập nhật thông tin sản phẩm (người dùng có thể cập nhật từng trường)
    public boolean updateProduct(int id, String name, String category, String description, Double price, Integer quantity) {
        Product p = findById(id);
        if (p == null) return false;
        if (name != null) p.setName(name);
        if (category != null) p.setCategory(category);
        if (description != null) p.setDescription(description);
        if (price != null) p.setPrice(price);
        if (quantity != null) p.setQuantity(quantity);
        return true;
    }

    // Hiển thị tất cả sản phẩm
    public void displayAll() {
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm rỗng.");
            return;
        }
        for (Product p : products) System.out.println(p);
    }

    // Hiển thị theo danh mục
    public void displayByCategory(String category) {
        boolean found = false;
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found) System.out.println("Không tìm thấy sản phẩm trong danh mục: " + category);
    }

    // Hiển thị sắp xếp theo giá (tăng dần hoặc giảm dần) - sử dụng giá sau giảm
    public void displaySortedByPrice(boolean ascending) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                double diff = o1.getPriceAfterDiscount() - o2.getPriceAfterDiscount();
                return ascending ? (diff < 0 ? -1 : (diff > 0 ? 1 : 0)) : (diff < 0 ? 1 : (diff > 0 ? -1 : 0));
            }
        });
        displayAll();
    }

    // Tính tổng giá trị hàng tồn kho cho từng danh mục
    public Map<String, Double> totalValuePerCategory() {
        Map<String, Double> totals = new HashMap<>();
        for (Product p : products) {
            double value = p.getPriceAfterDiscount() * p.getQuantity();
            totals.put(p.getCategory(), totals.getOrDefault(p.getCategory(), 0.0) + value);
        }
        return totals;
    }

    // Áp dụng giảm giá cho một sản phẩm
    public boolean applyDiscountToProduct(int id, double percent) {
        Product p = findById(id);
        if (p == null) return false;
        p.setDiscountPercent(percent);
        return true;
    }

    // Áp dụng giảm giá cho một danh mục (tất cả sản phẩm trong danh mục)
    public void applyDiscountToCategory(String category, double percent) {
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                p.setDiscountPercent(percent);
            }
        }
    }

    // Tính đặt hàng: kiểm tra tồn kho, trừ tồn, trả tổng tiền
    public double orderProduct(int id, int qty) throws IllegalArgumentException {
        Product p = findById(id);
        if (p == null) throw new IllegalArgumentException("Sản phẩm không tồn tại (ID:" + id + ")");
        if (qty <= 0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        if (p.getQuantity() < qty) throw new IllegalArgumentException("Không đủ tồn kho. Tồn: " + p.getQuantity());
        double total = roundTwoDecimals(qty * p.getPriceAfterDiscount());
        p.setQuantity(p.getQuantity() - qty);
        return total;
    }

    private double roundTwoDecimals(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // Một số sản phẩm mẫu để test nhanh
    public void seedSampleData() {
        addProduct("Sữa tắm A", "Mỹ phẩm", "Sữa tắm thơm mát", 85000, 30);
        addProduct("Kem dưỡng B", "Mỹ phẩm", "Dưỡng da ban đêm", 150000, 20);
        addProduct("Bánh quy", "Thực phẩm", "Bánh quy vị socola", 25000, 100);
        addProduct("Trà sữa", "Thực phẩm", "Trà sữa đóng chai", 30000, 50);
        addProduct("USB 16GB", "Điện tử", "USB nhỏ gọn", 120000, 15);
        addProduct("Laptop", "Điện tử", "Máy tính gaming", 12000000, 25);
        addProduct("Lego", "Đồ chơi", "Mô hình lắp ráp", 1200000, 40);
    }
}
