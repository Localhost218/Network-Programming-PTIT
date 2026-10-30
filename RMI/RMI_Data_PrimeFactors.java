package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_Data_PrimeFactors {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "primeFactors";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            Object resp = service.requestData(studentCode, qCode);
            int n = Integer.parseInt(resp.toString().trim());
            System.out.println("So N nhan duoc: " + n);

            List<Integer> factors = new ArrayList<>();
            int temp = n;
            for (int i = 2; i * i <= temp; i++) {
                while (temp % i == 0) {
                    factors.add(i);
                    temp /= i;
                }
            }
            if (temp > 1) {
                factors.add(temp);
            }

            System.out.println("Cac thua so nguyen to: " + factors);
            service.submitData(studentCode, qCode, factors);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
