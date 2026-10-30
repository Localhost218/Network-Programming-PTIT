package soap;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SOAP_RemoveDuplicates {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "removeDup";            

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

            // b. Xóa tất cả các phần tử trùng lặp, giữ lại lần xuất hiện đầu tiên
            Set<Integer> uniqueSet = new LinkedHashSet<>(listData);
            List<Integer> uniqueList = new ArrayList<>(uniqueSet);
            System.out.println("Dữ liệu sau khi loại bỏ trùng lặp: " + uniqueList);

            // c. Gửi kết quả trở lại server qua submitDataIntArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataIntArray(studentCode, qCode, uniqueList);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
