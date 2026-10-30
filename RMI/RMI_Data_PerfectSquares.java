package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_Data_PerfectSquares {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "perfectSquares";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            Object resp = service.requestData(studentCode, qCode);
            int n = Integer.parseInt(resp.toString().trim());
            System.out.println("N: " + n);

            List<Integer> squares = new ArrayList<>();
            for (int i = 1; i * i < n; i++) {
                squares.add(i * i);
            }

            System.out.println("Cac so chinh phuong nho hon " + n + ": " + squares);
            service.submitData(studentCode, qCode, squares);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
