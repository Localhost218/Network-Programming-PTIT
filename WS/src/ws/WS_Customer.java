package ws;

import java.util.ArrayList;
import java.util.List;

public class WS_Customer {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "WaGJ7jK2"; // Mã câu hỏi thực tế của bạn            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListCustomer
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Customer> listData = port.requestListCustomer(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " khách hàng):");
            for (Customer c : listData) {
                System.out.println(c.getCustomerId() + " | " + c.getLocation() + " | PurchaseCount: " + c.getPurchaseCount() + " | TotalSpent: " + c.getTotalSpent());
            }

            // b. Lọc và giữ lại các khách hàng có totalSpent > 5000 và purchaseCount >= 5
            List<Customer> filteredList = new ArrayList<>();
            for (Customer c : listData) {
                if (c.getTotalSpent() > 500 && c.getPurchaseCount() >= 4) {
                    filteredList.add(c);
                }
            }

            System.out.println("\n--- Danh sách sau khi lọc (" + filteredList.size() + " khách hàng tiềm năng): ---");
            for (Customer c : filteredList) {
                System.out.println(c.getCustomerId() + " | " + c.getLocation() + " | PurchaseCount: " + c.getPurchaseCount() + " | TotalSpent: " + c.getTotalSpent());
            }

            // c. Gửi danh sách khách hàng tiềm năng về server qua submitListCustomer
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListCustomer(studentCode, qCode, filteredList);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
