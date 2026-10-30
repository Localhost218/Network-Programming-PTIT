package soap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SOAP_InactiveCustomerY {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "inactiveCustY";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestListCustomerY
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            List<CustomerY> listData = port.requestListCustomerY(studentCode, qCode);

            if (listData == null || listData.isEmpty()) {
                System.out.println("Danh sách rỗng!");
                return;
            }

            System.out.println("Danh sách ban đầu (" + listData.size() + " khách hàng):");
            for (CustomerY c : listData) {
                System.out.println(c.getCustomerId() + " | Giao dịch cuối: " + c.getLastTransactionDate());
            }

            // b. Lọc khách hàng chưa có giao dịch nào trong vòng 6 tháng qua
            // (ngày giao dịch cuối trước thời điểm 6 tháng trước tính từ ngày hiện tại)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -6);
            Date sixMonthsAgo = cal.getTime();
            System.out.println("Mốc 6 tháng trước: " + sixMonthsAgo);

            List<CustomerY> inactiveList = new ArrayList<>();
            for (CustomerY c : listData) {
                if (c.getLastTransactionDate() != null) {
                    Date transDate = c.getLastTransactionDate().toGregorianCalendar().getTime();
                    if (transDate.before(sixMonthsAgo)) {
                        inactiveList.add(c);
                    }
                } else {
                    // Nếu chưa từng có giao dịch
                    inactiveList.add(c);
                }
            }

            System.out.println("\n--- Khách hàng không hoạt động trong 6 tháng (" + inactiveList.size() + " khách hàng): ---");
            for (CustomerY c : inactiveList) {
                System.out.println(c.getCustomerId() + " | Giao dịch cuối: " + c.getLastTransactionDate());
            }

            // c. Gửi danh sách khách hàng không hoạt động về server qua submitListCustomerY
            System.out.println("\n--- Đang gửi kết quả về Server ---");
            port.submitListCustomerY(studentCode, qCode, inactiveList);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
