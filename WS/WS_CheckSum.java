package ws;

import java.util.List;

public class WS_CheckSum {

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "checkSum";            

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

            // b. Đếm số lượng số nguyên tố và tính weighted checksum
            int primeCount = 0;
            int checksum = 0;
            int i = 0;
            for (Integer num : listData) {
                if (isPrime(num)) {
                    primeCount++;
                }
                checksum += (++i) * num;
                checksum %= 100000;
            }

            // c. Tạo chuỗi kết quả: "primeCount=<n>;checksum=<m>"
            String result = "primeCount=" + primeCount + ";checksum=" + checksum;
            System.out.println("Dữ liệu gửi lên: " + result);

            // d. Gửi kết quả trở lại server qua submitDataString
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataString(studentCode, qCode, result);
            System.out.println("Đã gửi thành công!");

            // e. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
