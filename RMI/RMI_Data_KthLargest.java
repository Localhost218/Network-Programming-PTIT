package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RMI_Data_KthLargest {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "dataKthLargest";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // a. Nhan chuoi dang: "mang; K", vi du: "3, 1, 5, 12, 2, 11; 3"
            String resp = (String) service.requestData(studentCode, qCode);
            System.out.println("Du lieu nhan: " + resp);

            String[] parts = resp.split(";");
            String[] arrStr = parts[0].trim().split("[,\s]+");
            int k = Integer.parseInt(parts[1].trim());

            List<Integer> list = new ArrayList<>();
            for (String s : arrStr) {
                if (!s.isEmpty()) list.add(Integer.parseInt(s.trim()));
            }

            // b. Tim phan tu lon thu K
            Collections.sort(list); // Tang dan
            int kthLargest = list.get(list.size() - k);
            System.out.println("Phan tu lon thu " + k + ": " + kthLargest);

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, kthLargest);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
