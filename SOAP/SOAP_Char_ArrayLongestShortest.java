package soap;

import java.util.List;

public class SOAP_Char_ArrayLongestShortest {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "arrayLongestShortest";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestStringArray để nhận mảng chuỗi từ server
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<String> listData = port.requestStringArray(studentCode, qCode);
            System.out.println("Mảng chuỗi nhận về: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Xác định từ có độ dài lớn nhất và nhỏ nhất (giữ từ đầu tiên nếu trùng độ dài)
            String longest = listData.get(0);
            String shortest = listData.get(0);

            for (String s : listData) {
                if (s.length() > longest.length()) {
                    longest = s;
                }
                if (s.length() < shortest.length()) {
                    shortest = s;
                }
            }

            System.out.println("Từ dài nhất: " + longest);
            System.out.println("Từ ngắn nhất: " + shortest);

            // c. Tạo chuỗi mới theo định dạng: "Longest; Shortest" (hoặc "longest;shortest")
            // Trong đề ví dụ: "pineapple;fig" hoặc "pineapple; fig"
            String result = longest + ";" + shortest;
            System.out.println("Chuỗi kết quả gửi đi: " + result);

            // d. Gửi lại kết quả bằng submitCharacterString
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitCharacterString(studentCode, qCode, result);
            System.out.println("Đã gửi thành công!");

            // e. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
