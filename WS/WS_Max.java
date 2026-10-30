package ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WS_Max {

    public static void main(String[] args) {
        // Thay bằng mã SV và mã câu hỏi thực tế của bạn
        String studentCode = "B23DCCN465"; 
        String qCode = "ElGYJH96";            

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

            // b. Tìm số lớn nhất có thể tạo được từ các số trong danh sách
            // Chuyển danh sách số sang danh sách String
            List<String> strList = new ArrayList<>();
            for (Integer num : listData) {
                strList.add(String.valueOf(num));
            }

            // Sắp xếp: với 2 chuỗi s1 và s2, ghép (s2 + s1) so sánh với (s1 + s2)
            Collections.sort(strList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    String order1 = s1 + s2;
                    String order2 = s2 + s1;
                    return order2.compareTo(order1); // Sắp xếp giảm dần
                }
            });

            // Ghép lại thành chuỗi kết quả
            StringBuilder sb = new StringBuilder();
            for (String s : strList) {
                sb.append(s);
            }
            String result = sb.toString();

            // Trường hợp nếu toàn số 0 (ví dụ "0000") thì kết quả là "0"
            if (result.startsWith("0")) {
                result = "0";
            }
            System.out.println("Kết quả chuỗi số lớn nhất: " + result);

            // c. Gửi kết quả trở lại server qua submitDataString
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataString(studentCode, qCode, result);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}