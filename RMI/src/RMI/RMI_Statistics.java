/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RMI;

/**
 *
 * @author ngtukien
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Locale;

public class RMI_Statistics {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "Va26VH2Z";

        try {
            // 1. Ket noi RMI Server
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service =
                    (DataService) registry.lookup("RMIDataService");

            // 2. Nhan chuoi CSV
            Object response =
                    service.requestData(studentCode, qCode);

            String csv = (String) response;
            System.out.println("Du lieu: " + csv);

            // 3. Chuyen CSV thanh mang so thuc
            String[] parts = csv.trim().split(",");
            int n = parts.length;

            if (n == 0 || (n == 1 && parts[0].trim().isEmpty())) {
                throw new IllegalArgumentException("Du lieu rong!");
            }

            double[] numbers = new double[n];
            double sum = 0.0;

            for (int i = 0; i < n; i++) {
                numbers[i] = Double.parseDouble(parts[i].trim());
                sum += numbers[i];
            }

            // 4. Tinh average
            double average = sum / n;

            // 5. Tinh standard deviation tong the
            double squaredDiffSum = 0.0;

            for (double x : numbers) {
                squaredDiffSum += Math.pow(x - average, 2);
            }

            double stddev = Math.sqrt(squaredDiffSum / n);

            // 6. Tinh P95
            double[] sorted = numbers.clone();
            Arrays.sort(sorted);

            int index = (int) Math.ceil(n * 0.95) - 1;
            double p95 = sorted[index];

            // 7. Dinh dang 2 chu so thap phan
            String result = String.format(
                    Locale.US,
                    "average=%.2f;stddev=%.2f;p95=%.2f",
                    average, stddev, p95
            );

            System.out.println("Ket qua: " + result);

            // 8. Gui ket qua ve server
            service.submitData(studentCode, qCode, result);

            System.out.println("Submit thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}