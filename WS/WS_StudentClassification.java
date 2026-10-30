package ws;

import java.util.ArrayList;
import java.util.List;

public class WS_StudentClassification {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "studentClassify";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListStudent
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Student> listData = port.requestListStudent(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " sinh viên):");
            for (Student s : listData) {
                System.out.println(s.getName() + " - Điểm: " + s.getScore());
            }

            // b. Phân nhóm sinh viên:
            // A: score >= 8.0
            // B: 6.5 <= score < 8.0
            // C: 5.0 <= score < 6.5
            // D: score < 5.0
            List<Student> groupA = new ArrayList<>();
            List<Student> groupB = new ArrayList<>();
            List<Student> groupC = new ArrayList<>();
            List<Student> groupD = new ArrayList<>();

            for (Student s : listData) {
                float score = s.getScore();
                if (score >= 8.0f) {
                    groupA.add(s);
                } else if (score >= 6.5f) {
                    groupB.add(s);
                } else if (score >= 5.0f) {
                    groupC.add(s);
                } else {
                    groupD.add(s);
                }
            }

            List<Student> classifiedList = new ArrayList<>();
            classifiedList.addAll(groupA);
            classifiedList.addAll(groupB);
            classifiedList.addAll(groupC);
            classifiedList.addAll(groupD);

            System.out.println("\n--- Danh sách sau khi phân nhóm theo thứ tự A -> B -> C -> D ---");
            for (Student s : classifiedList) {
                System.out.println(s.getName() + " - Điểm: " + s.getScore());
            }

            // c. Gửi danh sách sinh viên đã phân loại về server qua submitListStudent
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListStudent(studentCode, qCode, classifiedList);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
