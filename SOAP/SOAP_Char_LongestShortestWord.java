package soap;

public class SOAP_Char_LongestShortestWord {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "longestShortest";            

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

            // b. Tách các từ theo khoảng trắng
            String[] words = data.trim().split("\\s+");
            String longest = words[0];
            String shortest = words[0];

            for (String w : words) {
                if (w.length() > longest.length()) {
                    longest = w;
                }
                if (w.length() < shortest.length()) {
                    shortest = w;
                }
            }

            System.out.println("Từ dài nhất: " + longest);
            System.out.println("Từ ngắn nhất: " + shortest);

            // c. Tạo chuỗi mới theo định dạng: "[từ lớn nhất]; [từ nhỏ nhất]"
            String result = longest + "; " + shortest;
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
