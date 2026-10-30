package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.Set;

public class RMI_Obj_TicketCode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "ticketCode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            Ticket ticket = (Ticket) service.requestObject(studentCode, qCode);
            System.out.println("Ticket: " + ticket.getEventName() + ", SaleDate: " + ticket.getSaleDate());

            // 1. Chu cai dau va cuoi cua eventName in hoa
            String name = ticket.getEventName().trim();
            char first = Character.toUpperCase(name.charAt(0));
            char last = Character.toUpperCase(name.charAt(name.length() - 1));

            // 2. Ngay va thang tu saleDate dinh dang MMdd
            // Vi du: "15/06/2024" -> "0615"
            String saleDate = ticket.getSaleDate().trim();
            String mmdd = "";
            if (saleDate.contains("/")) {
                String[] p = saleDate.split("/");
                mmdd = p[1] + p[0];
            } else if (saleDate.contains("-")) {
                String[] p = saleDate.split("-");
                mmdd = p[1] + p[2];
            }

            // 3. Chu so lon nhat va nho nhat KHONG xuat hien trong saleDate
            Set<Integer> presentDigits = new HashSet<>();
            for (char c : saleDate.toCharArray()) {
                if (Character.isDigit(c)) {
                    presentDigits.add(c - '0');
                }
            }

            int minMissing = -1;
            int maxMissing = -1;
            for (int d = 0; d <= 9; d++) {
                if (!presentDigits.contains(d)) {
                    if (minMissing == -1) minMissing = d;
                    maxMissing = d;
                }
            }

            String ticketCode = "" + first + last + mmdd + maxMissing + minMissing;
            System.out.println("Generated ticketCode: " + ticketCode);

            ticket.setTicketCode(ticketCode);
            service.submitObject(studentCode, qCode, ticket);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
