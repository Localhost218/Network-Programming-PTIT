package ws;

public class WS_Char_Reverse {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "reverseStr";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            CharacterService service = new CharacterService();
            SoapCharacterService port = service.getSoapCharacterServicePort();

            // a. Triệu gọi phương thức requestString để nhận chuỗi từ server
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            String data = port.requestString(studentCode, qCode);
            System.out.println("Chuỗi ban đầu: " + data);

            if (data == null) {
                System.out.println("Chuỗi null!");
                return;
            }

            // b. Đảo ngược chuỗi
            String reversedString = new StringBuilder(data).reverse().toString();
            System.out.println("Chuỗi sau khi đảo ngược: " + reversedString);

            // c. Gửi lại kết quả bằng submitString
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitString(studentCode, qCode, reversedString);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
