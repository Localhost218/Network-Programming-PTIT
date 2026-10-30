package ws;

import java.util.ArrayList;
import java.util.List;

public class WS_PrimeFactors {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "primeFactors";            

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

            // b. Phân tích số nguyên thành các thừa số nguyên tố
            // (Thường bài toán cho 1 số ở phần tử đầu tiên hoặc phân tích tất cả các số)
            List<Integer> factors = new ArrayList<>();
            for (Integer num : listData) {
                int n = num;
                for (int i = 2; i * i <= n; i++) {
                    while (n % i == 0) {
                        factors.add(i);
                        n /= i;
                    }
                }
                if (n > 1) {
                    factors.add(n);
                }
            }
            System.out.println("Các thừa số nguyên tố: " + factors);

            // c. Gửi kết quả trở lại server qua submitDataIntArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataIntArray(studentCode, qCode, factors);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
