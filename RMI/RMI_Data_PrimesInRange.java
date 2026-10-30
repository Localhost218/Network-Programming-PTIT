package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_Data_PrimesInRange {
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "primesInRange";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            Object resp = service.requestData(studentCode, qCode);
            int n = Integer.parseInt(resp.toString().trim());
            System.out.println("N: " + n);

            List<Integer> primes = new ArrayList<>();
            for (int i = 2; i <= n; i++) {
                if (isPrime(i)) primes.add(i);
            }

            System.out.println("Cac so nguyen to <= " + n + ": " + primes);
            service.submitData(studentCode, qCode, primes);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
