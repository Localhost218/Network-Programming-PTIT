package soap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SOAP_PriorityProject {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "priorityProject";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListProject
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<Project> listData = port.requestListProject(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " dự án):");
            for (Project p : listData) {
                System.out.println(p.getProjectId() + " | " + p.getProjectName() + " | " + p.getCompletionPercentage() + "% | Hạn: " + p.getDueDate());
            }

            // b. Lọc các dự án có completionPercentage >= 80%
            // và hạn hoàn thành (dueDate) trong vòng 15 ngày tới tính từ hiện tại
            Date now = new Date();
            long fifteenDaysInMillis = 15L * 24 * 60 * 60 * 1000;

            List<Project> filteredList = new ArrayList<>();
            for (Project p : listData) {
                if (p.getCompletionPercentage() >= 80.0f && p.getDueDate() != null) {
                    Date dueDate = p.getDueDate().toGregorianCalendar().getTime();
                    long diff = dueDate.getTime() - now.getTime();
                    // Trong vòng 15 ngày tới: diff >= 0 và diff <= 15 ngày
                    if (diff >= 0 && diff <= fifteenDaysInMillis) {
                        filteredList.add(p);
                    }
                }
            }

            System.out.println("\n--- Dự án ưu tiên gần hoàn thành (" + filteredList.size() + " dự án): ---");
            for (Project p : filteredList) {
                System.out.println(p.getProjectId() + " | " + p.getProjectName() + " | " + p.getCompletionPercentage() + "% | Hạn: " + p.getDueDate());
            }

            // c. Gửi danh sách dự án gần hoàn thành về server qua submitListProject
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListProject(studentCode, qCode, filteredList);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
