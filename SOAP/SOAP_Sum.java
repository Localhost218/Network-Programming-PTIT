package soap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ngtukien
 */
public class SOAP_Sum {
    public static void main(String[] args) {
        // Thay bằng mã SV và mã câu hỏi thực tế của bạn
        String studentCode = "B23DCCN465"; 
        String qCode = "rHoNVt8z";            

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

            // b. Tính tổng
            int sum = 0;
            for (Integer num : listData) {
                sum += num;
            }
            // Chuyển số nguyên sum thành String (dùng 1 trong 2 cách dưới đều được)
            String result = String.valueOf(sum); 
            // hoặc: String result = sum + "";
            System.out.println("Kết quả tổng: " + result);

            // c. Gửi kết quả trở lại server qua submitDataString
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataString(studentCode, qCode, result);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
