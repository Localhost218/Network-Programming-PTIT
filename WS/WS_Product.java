package ws;

public class WS_Product {

    public static void main(String[] args) {
        String studentCode = "B23DCCN465"; 
        String qCode = "productFinalPrice";            

        try {
            // 1. Khởi tạo Service và lấy Port giao tiếp
            ObjectService service = new ObjectService();
            SoapObjectService port = service.getSoapObjectServicePort();

            // a. Triệu gọi phương thức requestProductY để nhận thông tin sản phẩm
            System.out.println("--- Đang lấy dữ liệu từ Server ---");
            ProductY p = port.requestProductY(studentCode, qCode);

            if (p == null) {
                System.out.println("Không nhận được đối tượng ProductY!");
                return;
            }

            System.out.println("Thông tin sản phẩm: Price=" + p.getPrice() 
                    + ", TaxRate=" + p.getTaxRate() 
                    + ", Discount=" + p.getDiscount());

            // b. Tính toán finalPrice = price * (1 + taxRate / 100) * (1 - discount / 100)
            float finalPrice = p.getPrice() * (1 + p.getTaxRate() / 100) * (1 - p.getDiscount() / 100);
            p.setFinalPrice(finalPrice);
            System.out.println("Giá cuối cùng (finalPrice): " + finalPrice);

            // c. Gửi đối tượng ProductY đã cập nhật về server qua submitProductY
            System.out.println("--- Đang gửi kết quả về Server ---");
            port.submitProductY(studentCode, qCode, p);
            System.out.println("Đã gửi thành công!");

            // d. Kết thúc chương trình client

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
