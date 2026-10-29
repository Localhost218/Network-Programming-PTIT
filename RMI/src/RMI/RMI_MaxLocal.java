package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_MaxLocal {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "QvSxhHho";

        try {
            // 1. Ket noi RMI Registry
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // 2. Nhan mang so nguyen tu server
            Object response = service.requestData(studentCode, qCode);
            List<Integer> numbers = new ArrayList<>();

            if (response instanceof int[]) {
                for (int x : (int[]) response) {
                    numbers.add(x);
                }
            } else if (response instanceof Integer[]) {
                for (Integer x : (Integer[]) response) {
                    numbers.add(x);
                }
            } else if (response instanceof List<?>) {
                for (Object x : (List<?>) response) {
                    numbers.add(((Number) x).intValue());
                }
            } else {
                throw new IllegalArgumentException(
                        "Kieu du lieu khong ho tro: "
                        + (response == null
                           ? "null"
                           : response.getClass().getName())
                );
            }

            System.out.println("Mang nhan duoc: " + numbers);

            // 3. Tim cac dinh cuc bo, vi tri bat dau tu 1
            List<Integer> result = new ArrayList<>();
            int n = numbers.size();

            for (int i = 0; i < n; i++) {
                boolean isPeak;

                if (n == 1) {
                    // Mang mot phan tu: khong co phan tu ke de so sanh
                    isPeak = true;
                } else if (i == 0) {
                    isPeak = numbers.get(i) > numbers.get(i + 1);
                } else if (i == n - 1) {
                    isPeak = numbers.get(i) > numbers.get(i - 1);
                } else {
                    isPeak = numbers.get(i) > numbers.get(i - 1)
                            && numbers.get(i) > numbers.get(i + 1);
                }

                if (isPeak) {
                    result.add(i + 1);
                }
            }

            System.out.println("Vi tri dinh cuc bo: " + result);

            // 4. Gui List<Integer> ve server
            service.submitData(studentCode, qCode, result);

            System.out.println("Da submit thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}