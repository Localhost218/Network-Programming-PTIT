package soap;

public class SOAP_Char_FilterAndReverse {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "filterReverse";            

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

            // b. Loại bỏ tất cả các ký tự không phải chữ cái (giữ lại [a-zA-Z])
            String cleaned = data.replaceAll("[^a-zA-Z]", "");
            System.out.println("Chuỗi sau khi chỉ giữ chữ cái: " + cleaned);

            // Đảo ngược chuỗi
            String reversed = new StringBuilder(cleaned).reverse().toString();
            System.out.println("Chuỗi sau khi đảo ngược: " + reversed);

            // c. Gửi lại kết quả bằng submitCharacterString
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitCharacterString(studentCode, qCode, reversed);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
