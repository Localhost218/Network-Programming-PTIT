package soap;

import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ngtukien
 */
public class SOAP_CheckSum {
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "0ZMKlDMc";            

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
            int primeCount = 0, checksum = 0, i = 0;
            for (Integer num: listData){
                if (isPrime(num)){
                    primeCount++;
                }
                checksum += (++i) * num;
                checksum %= 100000;
            }

            // c. Tạo danh sách chuỗi gồm EVEN=<n> và ODD=<m>
            StringBuilder result = new StringBuilder();
            result.append("primeCount=" + primeCount);
            result.append(";");
            result.append("checksum=" + checksum);

            System.out.println("Dữ liệu gửi lên: " + result);

            // d. Gửi kết quả trở lại server qua submitDataStringArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataString(studentCode, qCode, result.toString());
            System.out.println("Đã gửi thành công!");

            // e. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
