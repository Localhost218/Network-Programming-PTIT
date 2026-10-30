package soap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SOAP_Kth_LargestSmallest {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "kthSmallLarge";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            DataService service = new DataService();
            SoapDataService port = service.getSoapDataServicePort();

            // a. Triệu gọi phương thức getData để nhận danh sách số nguyên
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Integer> listData = port.getData(studentCode, qCode);
            System.out.println("Dữ liệu nhận về: " + listData);

            if (listData == null || listData.size() < 2) {
                System.out.println("Dữ liệu không đủ!");
                return;
            }

            // Phần tử đầu tiên là giá trị K
            int k = listData.get(0);
            List<Integer> remaining = new ArrayList<>(listData.subList(1, listData.size()));
            System.out.println("Giá trị K: " + k);
            System.out.println("Các phần tử còn lại: " + remaining);

            // b. Sắp xếp các phần tử còn lại để tìm lớn thứ K và nhỏ thứ K
            Collections.sort(remaining); // Tăng dần
            // Nhỏ thứ K (1-indexed): index = k - 1
            // Lớn thứ K (1-indexed): index = size - k
            int kthSmallest = remaining.get(k - 1);
            int kthLargest = remaining.get(remaining.size() - k);

            System.out.println("Phần tử lớn thứ " + k + ": " + kthLargest);
            System.out.println("Phần tử nhỏ thứ " + k + ": " + kthSmallest);

            // Kết quả: [lớn thứ K, nhỏ thứ K]
            List<Integer> result = Arrays.asList(kthLargest, kthSmallest);
            System.out.println("Mảng kết quả gửi đi: " + result);

            // c. Gửi kết quả trở lại server qua submitDataIntArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataIntArray(studentCode, qCode, result);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
