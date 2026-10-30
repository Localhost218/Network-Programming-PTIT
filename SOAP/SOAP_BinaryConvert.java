package soap;

import java.util.ArrayList;
import java.util.List;

public class SOAP_BinaryConvert {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "binaryConv";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            DataService service = new DataService();
            SoapDataService port = service.getSoapDataServicePort();

            // a. Triệu gọi phương thức getData để nhận danh sách số nguyên
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Integer> listData = port.getData(studentCode, qCode);
            System.out.println("Dữ liệu nhận về: " + listData);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            // b. Chuyển đổi từng số nguyên sang hệ nhị phân
            List<String> binaryList = new ArrayList<>();
            for (Integer num : listData) {
                binaryList.add(Integer.toBinaryString(num));
            }
            System.out.println("Dữ liệu nhị phân: " + binaryList);

            // c. Gửi kết quả trở lại server qua submitDataStringArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataStringArray(studentCode, qCode, binaryList);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
