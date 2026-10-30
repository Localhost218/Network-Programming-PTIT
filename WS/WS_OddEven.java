package ws;

import java.util.ArrayList;
import java.util.List;

public class WS_OddEven {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "oddEven";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            DataService service = new DataService();
            SoapDataService port = service.getSoapDataServicePort();

            // a. Triệu gọi phương thức getData để nhận danh sách số nguyên
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Integer> listData = port.getData(studentCode, qCode);
            System.out.println("Dữ liệu nhận về: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Đếm số lượng phần tử chẵn và lẻ
            int evenCount = 0;
            int oddCount = 0;
            for (Integer num : listData) {
                if (num % 2 == 0) {
                    evenCount++;
                } else {
                    oddCount++;
                }
            }

            System.out.println("Số lượng phần tử chẵn: " + evenCount);
            System.out.println("Số lượng phần tử lẻ: " + oddCount);

            // c. Tạo danh sách chuỗi gồm EVEN=<n> và ODD=<m>
            List<String> result = new ArrayList<>();
            result.add("EVEN=" + evenCount);
            result.add("ODD=" + oddCount);

            System.out.println("Dữ liệu gửi lên: " + result);

            // d. Gửi kết quả trở lại server qua submitDataStringArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataStringArray(studentCode, qCode, result);
            System.out.println("Đã gửi thành công!");

            // e. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
