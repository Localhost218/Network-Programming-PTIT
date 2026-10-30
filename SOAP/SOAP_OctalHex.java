package soap;

import java.util.ArrayList;
import java.util.List;

public class SOAP_OctalHex {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "octalHex";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            DataService service = new DataService();
            SoapDataService port = service.getSoapDataServicePort();

            // a. Triệu gọi phương thức getData để nhận dữ liệu
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Integer> listData = port.getData(studentCode, qCode);
            System.out.println("Dữ liệu nhận về: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            int num = listData.get(0);
            System.out.println("Số nguyên cần chuyển đổi: " + num);

            // b. Chuyển đổi sang hệ bát phân (octal) và thập lục phân (hex)
            String octalStr = Integer.toOctalString(num);
            String hexStr = Integer.toHexString(num);

            List<String> result = new ArrayList<>();
            result.add(octalStr);
            result.add(hexStr);
            System.out.println("Kết quả chuyển đổi [bát phân, thập lục phân]: " + result);

            // c. Gửi kết quả trở lại server qua submitDataStringArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataStringArray(studentCode, qCode, result);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
