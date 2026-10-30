package soap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SOAP_Char_SortByVowels {

    public static int countVowels(String s) {
        int count = 0;
        String vowels = "ueoaiUEOAI";
        for (char c : s.toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "sortByVowels";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestStringArray để nhận mảng chuỗi từ server
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<String> listData = port.requestStringArray(studentCode, qCode);
            System.out.println("Mảng chuỗi ban đầu: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Sắp xếp mảng theo số lượng nguyên âm tăng dần (stable sort giữ nguyên thứ tự ban đầu nếu bằng nhau)
            Collections.sort(listData, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return Integer.compare(countVowels(s1), countVowels(s2));
                }
            });
            System.out.println("Mảng sau khi sắp xếp theo số nguyên âm tăng dần: " + listData);

            // c. Gửi lại kết quả bằng submitCharacterStringArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitCharacterStringArray(studentCode, qCode, listData);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
