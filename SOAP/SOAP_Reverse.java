package soap;

import java.util.Collections;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ngtukien
 */
public class SOAP_Reverse {
    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "9XQXNjrr";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestCharacter
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            String data = port.requestString(studentCode, qCode);
            System.out.println("Chuỗi ban đầu: " + data);

            // b. Thực hiện xoay vòng phải k lần (với k là mã ASCII của ký tự đầu tiên)
            StringBuilder sb = new StringBuilder(data);
            sb.reverse();
            String reversedString = sb.toString();

            // c. Gửi lại kết quả bằng submitCharacterCharArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitString(studentCode, qCode, reversedString);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
        }
    }
}
