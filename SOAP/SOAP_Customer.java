/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package soap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ngtukien
 */
public class SOAP_Customer {
    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "frWWhwiL"; // Thay mã câu hỏi của đề bài            

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
            
            Collections.sort(listData, new Comparator<Customer>() {
                @Override
                public int compare(Customer c1, Customer c2) {
                    float ts1 = c1.getTotalSpent();
                    float ts2 = c2.getTotalSpent();

                    return Float.compare(ts2, ts1);
                }
            });

            // b. Lọc và giữ lại các khách hàng có totalSpent > 5000 và purchaseCount >= 5
            List<Customer> filteredList = new ArrayList<>();
            for (Customer c : listData) {
                if (c.getTotalSpent() >= 4000 && c.getPurchaseCount() >= 6) {
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
