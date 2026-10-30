package soap;

import java.util.Collections;
import java.util.List;

public class SOAP_Char_SortAlphabet {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "sortAlpha";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestCharacter để nhận mảng ký tự
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Integer> listData = port.requestCharacter(studentCode, qCode);
            System.out.println("Mảng ký tự ban đầu: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Sắp xếp mảng ký tự theo thứ tự từ điển (alphabetical order)
            Collections.sort(listData);
            System.out.println("Mảng sau khi sắp xếp từ điển: " + listData);

            // c. Gửi lại kết quả bằng submitCharacterCharArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitCharacterCharArray(studentCode, qCode, listData);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
