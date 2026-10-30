package ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WS_TopCustomerOrder {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "topCustomerOrder";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListOrder
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Order> listData = port.requestListOrder(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " đơn hàng):");
            for (Order o : listData) {
                System.out.println("CustomerId: " + o.getCustomerId() + " | Amount: " + o.getAmount() + " | Status: " + o.getStatus());
            }

            // b. Tính tổng giá trị đơn hàng cho từng khách hàng
            Map<String, Float> customerTotalMap = new HashMap<>();
            for (Order o : listData) {
                customerTotalMap.put(o.getCustomerId(), 
                        customerTotalMap.getOrDefault(o.getCustomerId(), 0f) + o.getAmount());
            }

            // Tìm khách hàng có tổng giá trị đơn hàng cao nhất
            String topCustomer = null;
            float maxTotal = -1f;
            for (Map.Entry<String, Float> entry : customerTotalMap.entrySet()) {
                if (entry.getValue() > maxTotal) {
                    maxTotal = entry.getValue();
                    topCustomer = entry.getKey();
                }
            }

            System.out.println("\nKhách hàng có tổng giá trị đơn hàng cao nhất: " + topCustomer + " (Tổng: " + maxTotal + ")");

            // Lọc ra các hóa đơn của khách hàng này
            List<Order> topOrders = new ArrayList<>();
            for (Order o : listData) {
                if (o.getCustomerId().equals(topCustomer)) {
                    topOrders.add(o);
                }
            }

            System.out.println("Danh sách đơn hàng của khách hàng cao nhất:");
            for (Order o : topOrders) {
                System.out.println("CustomerId: " + o.getCustomerId() + " | Amount: " + o.getAmount() + " | Status: " + o.getStatus());
            }

            // c. Gửi danh sách hóa đơn về server qua submitListOrder
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListOrder(studentCode, qCode, topOrders);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
