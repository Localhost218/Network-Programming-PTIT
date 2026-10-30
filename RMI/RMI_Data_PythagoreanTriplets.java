package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RMI_Data_PythagoreanTriplets {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "pythagoreanTriplets";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            Object resp = service.requestData(studentCode, qCode);
            int n = Integer.parseInt(resp.toString().trim());
            System.out.println("N: " + n);

            List<List<Integer>> result = new ArrayList<>();
            for (int a = 1; a <= n; a++) {
                for (int b = a + 1; b <= n; b++) {
                    int c2 = a * a + b * b;
                    int c = (int) Math.sqrt(c2);
                    if (c * c == c2 && c <= n && b < c) {
                        result.add(Arrays.asList(a, b, c));
                    }
                }
            }

            System.out.println("Cac bo ba Pythagore: " + result);
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
