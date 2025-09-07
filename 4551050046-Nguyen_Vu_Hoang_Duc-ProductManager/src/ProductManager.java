// ProductManager.java
import java.util.Map;
import java.util.Scanner;

public class ProductManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventory inventory = new Inventory();
        inventory.seedSampleData();

        // HIỂN THỊ SẢN PHẨM KHI BẮT ĐẦU
        System.out.println("SẢN PHẨM HIỆN CÓ:");
        inventory.displayAll();
        System.out.println("==============================================\n");

        boolean exit = false;
        while (!exit) {
            showMenu();
            System.out.print("Chọn chức năng (số): ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": // Cập nhật thông tin sản phẩm
                    updateProductUI(sc, inventory);
                    break;
                case "2": // Hiển thị danh sách theo giá
                    displayByPriceUI(sc, inventory);
                    break;
                case "3": // Hiển thị danh sách theo danh mục
                    displayByCategoryUI(sc, inventory);
                    break;
                case "4": // Tính tổng giá trị hàng tồn kho theo danh mục
                    totalValueUI(inventory);
                    break;
                case "5": // Áp dụng giảm giá
                    discountUI(sc, inventory);
                    break;
                case "6": // Đặt hàng
                    orderUI(sc, inventory);
                    break;
                case "7":
                    System.out.println("Thoát chương trình");
                    exit = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập lại.");
            }

            // In lại danh sách sau khi dùng chức năng 1
            if (!exit && "1".equals(choice)) {
                System.out.println("\n===== DANH SÁCH SẢN PHẨM SAU CẬP NHẬT =====");
                inventory.displayAll();
                System.out.println("====================================================\n");
            }

            System.out.println();
        }
        sc.close();
    }

    static void showMenu() {
        System.out.println("========= QUẢN LÝ SẢN PHẨM =========");
        System.out.println("1. Cập nhật thông tin sản phẩm (giá, mô tả, số lượng,...)");
        System.out.println("2. Hiển thị danh sách sản phẩm theo giá (tăng/giảm)");
        System.out.println("3. Hiển thị danh sách sản phẩm theo danh mục");
        System.out.println("4. Tính tổng giá trị hàng tồn kho cho từng danh mục");
        System.out.println("5. Áp dụng giảm giá");
        System.out.println("6. Đặt hàng");
        System.out.println("7. Thoát");
        System.out.println("====================================");
    }

    static void updateProductUI(Scanner sc, Inventory inv) {
        try {
            System.out.print("Nhập ID sản phẩm cần cập nhật: ");
            int id = Integer.parseInt(sc.nextLine());
            Product p = inv.findById(id);
            if (p == null) {
                System.out.println("Không tìm thấy sản phẩm với ID " + id);
                return;
            }
            System.out.println("Sản phẩm hiện tại:");
            System.out.println(p);
            System.out.println("Bỏ trống nếu không muốn thay đổi trường đó.");

            System.out.print("Tên mới: ");
            String name = sc.nextLine(); if (name.isEmpty()) name = null;
            System.out.print("Danh mục mới: ");
            String cat = sc.nextLine(); if (cat.isEmpty()) cat = null;
            System.out.print("Mô tả mới: ");
            String desc = sc.nextLine(); if (desc.isEmpty()) desc = null;
            System.out.print("Giá mới (VND): ");
            String priceStr = sc.nextLine();
            Double price = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);
            System.out.print("Số lượng mới: ");
            String qtyStr = sc.nextLine();
            Integer qty = qtyStr.isEmpty() ? null : Integer.parseInt(qtyStr);

            boolean ok = inv.updateProduct(id, name, cat, desc, price, qty);
            if (ok) System.out.println("Cập nhật thành công.");
            else System.out.println("Cập nhật thất bại.");
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật. Vui lòng kiểm tra dữ liệu.");
        }
    }

    static void displayByPriceUI(Scanner sc, Inventory inv) {
        System.out.print("Sắp xếp theo giá: 1 - Tăng dần, 2 - Giảm dần : ");
        String t = sc.nextLine().trim();
        if (t.equals("1")) inv.displaySortedByPrice(true);
        else if (t.equals("2")) inv.displaySortedByPrice(false);
        else System.out.println("Lựa chọn không hợp lệ.");
    }

    static void displayByCategoryUI(Scanner sc, Inventory inv) {
        System.out.print("Nhập danh mục cần hiển thị: ");
        String cat = sc.nextLine();
        inv.displayByCategory(cat);
    }

    static void totalValueUI(Inventory inv) {
        Map<String, Double> totals = inv.totalValuePerCategory();
        if (totals.isEmpty()) {
            System.out.println("Không có sản phẩm nào.");
            return;
        }
        System.out.println("Tổng giá trị tồn kho theo danh mục (đã tính giảm giá nếu có):");
        for (Map.Entry<String, Double> e : totals.entrySet()) {
            System.out.printf("  - %s : %.2f VND\n", e.getKey(), e.getValue());
        }
    }

    static void discountUI(Scanner sc, Inventory inv) {
        System.out.println("1. Giảm giá cho sản phẩm theo ID");
        System.out.println("2. Giảm giá cho tất cả sản phẩm trong danh mục");
        System.out.print("Chọn (1/2): ");
        String c = sc.nextLine();
        if (c.equals("1")) {
            try {
                System.out.print("Nhập ID sản phẩm: ");
                int id = Integer.parseInt(sc.nextLine());
                System.out.print("Nhập phần trăm giảm giá (0-100): ");
                double p = Double.parseDouble(sc.nextLine());
                boolean ok = inv.applyDiscountToProduct(id, p);
                if (ok) System.out.println("Áp dụng giảm giá thành công.");
                else System.out.println("Không tìm thấy sản phẩm.");
            } catch (Exception e) {
                System.out.println("Dữ liệu không hợp lệ.");
            }
        } else if (c.equals("2")) {
            System.out.print("Nhập danh mục: ");
            String cat = sc.nextLine();
            System.out.print("Nhập phần trăm giảm giá (0-100): ");
            try {
                double p = Double.parseDouble(sc.nextLine());
                inv.applyDiscountToCategory(cat, p);
                System.out.println("Đã áp dụng giảm giá cho danh mục (nếu có sản phẩm).");
            } catch (Exception e) {
                System.out.println("Dữ liệu không hợp lệ.");
            }
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
        }
    }

    static void orderUI(Scanner sc, Inventory inv) {
        try {
            System.out.print("Nhập ID sản phẩm đặt hàng: ");
            int id = Integer.parseInt(sc.nextLine());
            Product p = inv.findById(id);
            if (p == null) {
                System.out.println("Không tìm thấy sản phẩm.");
                return;
            }
            System.out.println("Sản phẩm chọn:");
            System.out.println(p);
            System.out.print("Nhập số lượng muốn đặt: ");
            int qty = Integer.parseInt(sc.nextLine());
            double total = inv.orderProduct(id, qty);
            System.out.printf("Đặt hàng thành công. Tổng tiền: %.2f VND\n", total);
            System.out.println("Cập nhật tồn kho: còn " + inv.findById(id).getQuantity() + " sản phẩm.");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi đặt hàng: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Dữ liệu không hợp lệ.");
        }
    }
}
