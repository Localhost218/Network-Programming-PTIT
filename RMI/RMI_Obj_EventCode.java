package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Obj_EventCode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "eventCode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            Event event = (Event) service.requestObject(studentCode, qCode);
            System.out.println("Event: " + event.getEventName() + ", Attendees: " + event.getExpectedAttendance());

            // 1. Attendance prefix: >= 1000 -> "L", 500..999 -> "M", < 500 -> "S"
            int att = event.getExpectedAttendance();
            String prefix = att >= 1000 ? "L" : (att >= 500 ? "M" : "S");

            // 2. Chu cai dau va cuoi cua eventName in hoa
            String name = event.getEventName().trim();
            char first = Character.toUpperCase(name.charAt(0));
            char last = Character.toUpperCase(name.charAt(name.length() - 1));

            // 3. Ngay va thang tu eventDate (ddMM)
            // eventDate co the co dang "2024-05-12" hoac "12/05/2024"
            String dateStr = event.getEventDate().trim();
            String ddMM = "";
            if (dateStr.contains("-")) {
                // "yyyy-MM-dd"
                String[] p = dateStr.split("-");
                ddMM = p[2] + p[1];
            } else if (dateStr.contains("/")) {
                // "dd/MM/yyyy"
                String[] p = dateStr.split("/");
                ddMM = p[0] + p[1];
            }

            String eventCode = prefix + first + last + ddMM;
            System.out.println("Generated eventCode: " + eventCode);

            event.setEventCode(eventCode);
            service.submitObject(studentCode, qCode, event);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
