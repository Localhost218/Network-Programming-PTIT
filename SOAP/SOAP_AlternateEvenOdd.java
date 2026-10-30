package soap;

import java.util.ArrayList;
import java.util.List;

public class SOAP_AlternateEvenOdd {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "evenOddAlt";            

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

            // b. Tách các phần tử chẵn và lẻ
            List<Integer> evens = new ArrayList<>();
            List<Integer> odds = new ArrayList<>();
            for (Integer num : listData) {
                if (num % 2 == 0) {
                    evens.add(num);
                } else {
                    odds.add(num);
                }
            }

            // Xen kẽ chẵn - lẻ: chẵn trước, lẻ sau
            List<Integer> alternatedList = new ArrayList<>();
            int i = 0, j = 0;
            while (i < evens.size() && j < odds.size()) {
                alternatedList.add(evens.get(i++));
                alternatedList.add(odds.get(j++));
            }
            // Thêm các phần tử dư thừa vào cuối mảng
            while (i < evens.size()) {
                alternatedList.add(evens.get(i++));
            }
            while (j < odds.size()) {
                alternatedList.add(odds.get(j++));
            }

            System.out.println("Mảng sau khi xen kẽ chẵn-lẻ: " + alternatedList);

            // c. Gửi kết quả trở lại server qua submitDataIntArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataIntArray(studentCode, qCode, alternatedList);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
