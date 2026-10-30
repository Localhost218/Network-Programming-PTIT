package soap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SOAP_CountFrequency {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "countFreq";            

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

            // b. Đếm số lần xuất hiện của từng phần tử (giữ thứ tự xuất hiện ban đầu)
            Map<Integer, Integer> freqMap = new LinkedHashMap<>();
            for (Integer num : listData) {
                freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
            }

            // Tạo danh sách cặp [phần tử, tần suất] phẳng để gửi qua submitDataIntArray
            List<Integer> flatResult = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
                flatResult.add(entry.getKey());
                flatResult.add(entry.getValue());
            }
            System.out.println("Kết quả [phần tử, tần suất]: " + flatResult);

            // c. Gửi kết quả trở lại server qua submitDataIntArray
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitDataIntArray(studentCode, qCode, flatResult);
            System.out.println("Gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
