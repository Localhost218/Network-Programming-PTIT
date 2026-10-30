package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Obj_OrderCode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "orderCode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            Order order = (Order) service.requestObject(studentCode, qCode);
            System.out.println("Order: " + order.getCustomerCode() + ", Type: " + order.getShippingType());

            // 1. Hai ky tu dau cua shippingType in hoa
            String ship = order.getShippingType().trim().toUpperCase();
            String shipPrefix = ship.length() >= 2 ? ship.substring(0, 2) : ship;

            // 2. Ba ky tu cuoi cua customerCode
            String cust = order.getCustomerCode().trim();
            String custSuffix = cust.length() >= 3 ? cust.substring(cust.length() - 3) : cust;

            // 3. Ngay va thang tu orderDate (ddMM)
            // Vi du "2023-10-05" -> "0510"
            String dateStr = order.getOrderDate().trim();
            String ddMM = "";
            if (dateStr.contains("-")) {
                String[] p = dateStr.split("-");
                ddMM = p[2] + p[1];
            } else if (dateStr.contains("/")) {
                String[] p = dateStr.split("/");
                ddMM = p[0] + p[1];
            }

            String orderCode = shipPrefix + custSuffix + ddMM;
            System.out.println("Generated orderCode: " + orderCode);

            order.setOrderCode(orderCode);
            service.submitObject(studentCode, qCode, order);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
