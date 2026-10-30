package ws;

import java.util.ArrayList;
import java.util.List;

public class WS_Char_MaskLog {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "maskLog";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestStringArray để nhận danh sách chuỗi log
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<String> listData = port.requestStringArray(studentCode, qCode);
            System.out.println("Danh sách log nhận về: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Che thông tin email, phone, token và phân loại theo ERROR, WARN, INFO
            List<String> errors = new ArrayList<>();
            List<String> warns = new ArrayList<>();
            List<String> infos = new ArrayList<>();
            List<String> others = new ArrayList<>();

            for (String line : listData) {
                // Che email, số điện thoại và token
                String masked = line.replaceAll("email=\\S+", "email=[EMAIL]")
                                    .replaceAll("phone=\\S+", "phone=[PHONE]")
                                    .replaceAll("token=\\S+", "token=[TOKEN]");

                // Phân loại theo mức độ log
                if (masked.startsWith("ERROR")) {
                    errors.add(masked);
                } else if (masked.startsWith("WARN")) {
                    warns.add(masked);
                } else if (masked.startsWith("INFO")) {
                    infos.add(masked);
                } else {
                    others.add(masked);
                }
            }

            // Ghép lại theo thứ tự: ERROR -> WARN -> INFO -> khác
            List<String> result = new ArrayList<>();
            result.addAll(errors);
            result.addAll(warns);
            result.addAll(infos);
            result.addAll(others);

            System.out.println("\n--- Danh sách sau khi xử lý ---");
            for (String s : result) {
                System.out.println(s);
            }

            // c. Triệu gọi submitStringArray để gửi danh sách chuỗi đã xử lý về server
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitStringArray(studentCode, qCode, result);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
