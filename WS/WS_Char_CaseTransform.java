package ws;

import java.util.ArrayList;
import java.util.List;

public class WS_Char_CaseTransform {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "caseTransform";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestString để nhận chuỗi từ server
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            String data = port.requestString(studentCode, qCode);
            System.out.println("Chuỗi ban đầu: " + data);

            if (data == null || data.trim().isEmpty()) {
                System.out.println("Chuỗi rỗng!");
                return;
            }

            // Tách các từ theo khoảng trắng hoặc dấu gạch dưới
            String[] rawWords = data.trim().split("[\\s_]+");
            List<String> words = new ArrayList<>();
            for (String w : rawWords) {
                if (!w.isEmpty()) {
                    words.add(w.toLowerCase());
                }
            }

            // b. Chuyển đổi sang 3 định dạng:
            // 1. PascalCase: Mỗi từ viết hoa chữ cái đầu, viết liền nhau
            StringBuilder pascalCase = new StringBuilder();
            for (String w : words) {
                pascalCase.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
            }

            // 2. camelCase: Từ đầu viết thường, các từ sau viết hoa chữ cái đầu, viết liền
            StringBuilder camelCase = new StringBuilder();
            for (int i = 0; i < words.size(); i++) {
                String w = words.get(i);
                if (i == 0) {
                    camelCase.append(w);
                } else {
                    camelCase.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
                }
            }

            // 3. snake_case: Các từ viết thường, nối nhau bằng dấu gạch dưới "_"
            String snakeCase = String.join("_", words);

            System.out.println("PascalCase: " + pascalCase);
            System.out.println("camelCase: " + camelCase);
            System.out.println("snake_case: " + snakeCase);

            List<String> result = new ArrayList<>();
            result.add(pascalCase.toString());
            result.add(camelCase.toString());
            result.add(snakeCase);

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
}
