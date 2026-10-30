package ws;

import java.util.Calendar;

public class WS_EmployeeWorkingDays {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "empWorkingDays";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestEmployee
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            Employee emp = port.requestEmployee(studentCode, qCode);

            if (emp == null || emp.getStartDate() == null || emp.getEndDate() == null) {
                System.out.println("Dữ liệu nhân viên không hợp lệ!");
                return;
            }

            System.out.println("StartDate: " + emp.getStartDate());
            System.out.println("EndDate: " + emp.getEndDate());

            // b. Tính số ngày làm việc loại trừ thứ 7 và Chủ Nhật (tính cả 2 mốc)
            Calendar startCal = emp.getStartDate().toGregorianCalendar();
            Calendar endCal = emp.getEndDate().toGregorianCalendar();

            // Chuẩn hóa giờ về 0 để so sánh chính xác theo ngày
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            startCal.set(Calendar.MILLISECOND, 0);

            endCal.set(Calendar.HOUR_OF_DAY, 0);
            endCal.set(Calendar.MINUTE, 0);
            endCal.set(Calendar.SECOND, 0);
            endCal.set(Calendar.MILLISECOND, 0);

            int workingDays = 0;
            while (!startCal.after(endCal)) {
                int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                    workingDays++;
                }
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            }

            System.out.println("Số ngày làm việc tính được: " + workingDays);
            emp.setWorkingDays(workingDays);

            // c. Gửi đối tượng Employee về server qua submitEmployee
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitEmployee(studentCode, qCode, emp);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
