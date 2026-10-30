package ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WS_PriorityOrderY {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "priorityOrderY";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListOrderY
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<OrderY> listData = port.requestListOrderY(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " đơn hàng):");
            for (OrderY o : listData) {
                System.out.println(o.getOrderId() + " | " + o.getStatus() + " | " + o.getOrderDate());
            }

            // b. Lọc các đơn hàng có trạng thái "pending" hoặc "processing"
            // và ngày đặt hàng trong vòng 30 ngày trở lại từ ngày hiện tại
            Date now = new Date();
            long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;

            List<OrderY> filteredList = new ArrayList<>();
            for (OrderY o : listData) {
                String status = o.getStatus();
                if ("pending".equalsIgnoreCase(status) || "processing".equalsIgnoreCase(status)) {
                    if (o.getOrderDate() != null) {
                        Date orderDate = o.getOrderDate().toGregorianCalendar().getTime();
                        long diff = now.getTime() - orderDate.getTime();
                        // Trong vòng 30 ngày trở lại từ hiện tại: diff >= 0 và diff <= 30 ngày
                        if (diff >= 0 && diff <= thirtyDaysInMillis) {
                            filteredList.add(o);
                        }
                    }
                }
            }

            System.out.println("\n--- Danh sách đơn hàng ưu tiên sau khi lọc (" + filteredList.size() + " đơn): ---");
            for (OrderY o : filteredList) {
                System.out.println(o.getOrderId() + " | " + o.getStatus() + " | " + o.getOrderDate());
            }

            // c. Gửi danh sách đã lọc về server qua submitListOrderY
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListOrderY(studentCode, qCode, filteredList);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
