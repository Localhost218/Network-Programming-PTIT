package soap;

import java.util.ArrayList;
import java.util.List;

public class SOAP_SimplifyFraction {

    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "simplifyFrac";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            DataService service = new DataService();
            SoapDataService port = service.getSoapDataServicePort();

            // a. Triệu gọi phương thức getDataDouble để nhận số thập phân
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            double val = port.getDataDouble(studentCode, qCode);
            System.out.println("Số thập phân nhận về: " + val);

            // b. Chuyển đổi số thập phân thành phân số đơn giản nhất [tử số, mẫu số]
            String s = String.valueOf(val);
            int decimalDigits = 0;
            if (s.contains(".")) {
                decimalDigits = s.length() - s.indexOf('.') - 1;
            }

            int denominator = (int) Math.pow(10, decimalDigits);
            int numerator = (int) Math.round(val * denominator);

            int commonGcd = gcd(numerator, denominator);
            numerator /= commonGcd;
            denominator /= commonGcd;

            List<Integer> result = new ArrayList<>();
            result.add(numerator);
            result.add(denominator);
            System.out.println("Phân số tối giản [tử số, mẫu số]: " + result);

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
