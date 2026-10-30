package RMI;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ngtukien
 */
import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_TicketSla {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "7NX7mukc";

        try {
            // 1. Ket noi Registry va lay remote service
            Registry registry = LocateRegistry.getRegistry(host);

            ObjectService service =
                    (ObjectService) registry.lookup("RMIObjectService");

            // 2. Nhan doi tuong TicketSla
            Serializable response =
                    service.requestObject(studentCode, qCode);

            TicketSla ticket = (TicketSla) response;

            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Priority: " + ticket.getPriority());
            System.out.println("Opened hours ago: "
                    + ticket.getOpenedHoursAgo());

            // 3. Xac dinh nguong SLA theo priority
            String priority = ticket.getPriority();
            int hours = ticket.getOpenedHoursAgo();
            int threshold;

            switch (priority) {
                case "CRITICAL":
                    threshold = 2;
                    break;
                case "HIGH":
                    threshold = 8;
                    break;
                case "MEDIUM":
                    threshold = 24;
                    break;
                case "LOW":
                    threshold = 72;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Priority khong hop le: " + priority);
            }

            // 4. Cap nhat breached va action
            boolean breached = hours > threshold;
            ticket.setBreached(breached);

            if (!breached) {
                ticket.setAction("MONITOR");
            } else if ("CRITICAL".equals(priority) || hours > 96) {
                ticket.setAction("ESCALATE_L2");
            } else {
                ticket.setAction("ESCALATE_L1");
            }

            System.out.println("Breached: " + ticket.isBreached());
            System.out.println("Action: " + ticket.getAction());

            // 5. Gui doi tuong da cap nhat ve server
            service.submitObject(studentCode, qCode, ticket);

            System.out.println("Submit thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
