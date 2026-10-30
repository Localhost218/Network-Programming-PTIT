package soap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SOAP_Divisors {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "divisors";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            DataService service = new DataService();
            SoapDataService port = service.getSoapDataServicePort();

            // a. Triệu gọi phương thức getDataDouble để nhận số thập phân
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            double val = port.getDataDouble(studentCode, qCode);
            System.out.println("Số thập phân nhận về: " + val);

            // b. Lấy phần nguyên và tìm tất cả các ước số tăng dần
            int n = (int) val;
            System.out.println("Phần nguyên: " + n);

            List<Integer> divisors = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (n % i == 0) {
                    divisors.add(i);
                }
            }

            // Mảng kết quả: phần tử đầu tiên là số lượng ước số, theo sau là các ước số
            List<Integer> result = new ArrayList<>();
            result.add(divisors.size());
            result.addAll(divisors);
            System.out.println("Kết quả [số lượng ước, các ước...]: " + result);

            // c. Gửi kết quả trở lại server qua submitDataIntArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataIntArray(studentCode, qCode, result);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
