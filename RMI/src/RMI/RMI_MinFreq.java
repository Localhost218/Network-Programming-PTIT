package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMI_MinFreq {

    public static void main(String[] args) {
        String host = "36.50.135.242"; // Thay bằng IP Server thi thực tế hoặc "localhost"
        String studentCode = "B23DCCN465"; // Thay bằng mã sinh viên của bạn
        String qCode = "LbRRZx4X";        // Thay bằng mã câu hỏi

        try {
            // Bước 1: Kết nối tới Registry Server và lấy đối tượng từ xa
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Triệu gọi requestData nhận mảng byte
            byte[] data = service.requestData(studentCode, qCode);
            System.out.println("Dữ liệu nhận được: " + Arrays.toString(data));

            if (data == null || data.length == 0) {
                System.out.println("Mảng dữ liệu rỗng!");
                return;
            }

            // b. Tìm phần tử có số lần xuất hiện ít nhất
            // Dùng LinkedHashMap để vừa đếm số lần xuất hiện, vừa giữ nguyên thứ tự xuất hiện đầu tiên
            Map<Byte, Integer> freqMap = new LinkedHashMap<>();
            for (byte b : data) {
                freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);
            }

            byte minElement = data[0];
            int minCount = Integer.MAX_VALUE;

            for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
                // Dùng dấu '<' để nếu bằng số lần thì luôn giữ phần tử xuất hiện trước
                if (entry.getValue() < minCount) {
                    minCount = entry.getValue();
                    minElement = entry.getKey();
                }
            }

            // Tạo mảng kết quả: [phần tử, số lần xuất hiện]
            byte[] result = new byte[] { minElement, (byte) minCount };
            System.out.println("Kết quả gửi về server: " + Arrays.toString(result));

            // c. Triệu gọi submitData để gửi kết quả về Server
            service.submitData(studentCode, qCode, result);
            System.out.println("Đã submit thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}