package ws;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WS_TopStudentYBySubject {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "topStudentY";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListStudentY
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<StudentY> listData = port.requestListStudentY(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " sinh viên):");
            for (StudentY s : listData) {
                System.out.println(s.getStudentId() + " | " + s.getName() + " | Môn: " + s.getSubject() + " | Điểm: " + s.getScore());
            }

            // b. Lọc và giữ lại sinh viên có điểm cao nhất theo từng môn học
            Map<String, StudentY> topPerSubject = new LinkedHashMap<>();
            for (StudentY s : listData) {
                String subject = s.getSubject();
                if (!topPerSubject.containsKey(subject) || s.getScore() > topPerSubject.get(subject).getScore()) {
                    topPerSubject.put(subject, s);
                }
            }

            List<StudentY> resultList = new ArrayList<>(topPerSubject.values());

            System.out.println("\n--- Sinh viên có điểm cao nhất theo từng môn học: ---");
            for (StudentY s : resultList) {
                System.out.println(s.getStudentId() + " | " + s.getName() + " | Môn: " + s.getSubject() + " | Điểm: " + s.getScore());
            }

            // c. Gửi danh sách đã lọc về server qua submitListStudentY
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListStudentY(studentCode, qCode, resultList);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
