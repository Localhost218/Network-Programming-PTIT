package soap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ngtukien
 */
public class SOAP_Group {
    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "e9HWmR26";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestCharacter
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<String> listData = port.requestStringArray(studentCode, qCode);
            System.out.println("Mảng ký tự ban đầu: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Phân loại các từ theo số lượng nguyên âm (giảm dần)
            Map <Integer, List<String>> map = new TreeMap<>();
            for (String word : listData) {
                int count = countVowels(word);
                if (!map.containsKey(count)) {
                    map.put(count, new java.util.ArrayList<>());
                }
                map.get(count).add(word.trim());
            }

            // Với mỗi nhóm, sắp xếp từ điển và thêm từng từ vào danh sách kết quả (KHÔNG gộp chuỗi)
            List<String> result = new java.util.ArrayList<>();
            for (List<String> group : map.values()) {
                Collections.sort(group); // Sắp xếp theo thứ tự từ điển
                result.addAll(group);    // Thêm từng từ riêng biệt vào kết quả
            }
            System.out.println("Kết quả danh sách các từ: " + result);

            // c. Gửi lại kết quả bằng submitCharacterStringArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitCharacterStringArray(studentCode, qCode, result);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hàm đếm số lượng nguyên âm (u, e, o, a, i cả hoa lẫn thường)
    private static int countVowels(String s) {
        int count = 0;
        String vowels = "ueoaiUEOAI";
        for (char c : s.toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }
}
