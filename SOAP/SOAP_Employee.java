package soap;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SOAP_Employee {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "4gPGgJiU"; // Thay mã câu hỏi của đề bài            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListEmployeeY
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<EmployeeY> listData = port.requestListEmployeeY(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Số lượng nhân viên nhận về: " + listData.size());
            for (EmployeeY emp : listData) {
                System.out.println(emp.getName() + " - " + emp.getStartDate());
            }

            // b. Sắp xếp danh sách EmployeeY theo thâm niên từ cao đến thấp
            // (Ngày bắt đầu làm việc sớm nhất / cũ nhất sẽ đứng đầu danh sách)
            // Collections.sort / listData.sort trong Java là stable sort: nếu cùng ngày sẽ giữ nguyên thứ tự ban đầu
            Collections.sort(listData, new Comparator<EmployeeY>() {
                @Override
                public int compare(EmployeeY e1, EmployeeY e2) {
                    Date d1 = e1.getStartDate().toGregorianCalendar().getTime();
                    Date d2 = e2.getStartDate().toGregorianCalendar().getTime();
                    return d1.compareTo(d2); // d1 sớm hơn d2 thì d1 đứng trước
                }
            });

            System.out.println("\n--- Sau khi sắp xếp ---");
            for (EmployeeY emp : listData) {
                System.out.println(emp.getName() + " - " + emp.getStartDate());
            }

            // c. Gửi danh sách đã sắp xếp về server qua submitListEmployeeY
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListEmployeeY(studentCode, qCode, listData);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
