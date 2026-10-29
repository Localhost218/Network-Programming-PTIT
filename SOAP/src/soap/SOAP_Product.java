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
public class SOAP_Product {
    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "Dh5hji6S"; // Mã câu hỏi thực tế của bạn            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListCustomer
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            ProductY p = port.requestProductY(studentCode, qCode);

            float finalPrice = p.price * (1 + p.taxRate / 100) * (1 - p.discount / 100);
            p.setFinalPrice(finalPrice);

            // c. Gửi danh sách khách hàng tiềm năng về server qua submitListCustomer
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitProductY(studentCode, qCode, p);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
