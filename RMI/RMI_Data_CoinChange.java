package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_Data_CoinChange {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "coinChange";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            Object resp = service.requestData(studentCode, qCode);
            int amount = Integer.parseInt(resp.toString().trim());
            System.out.println("Amount: " + amount);

            int[] coins = {10, 5, 2, 1};
            List<String> used = new ArrayList<>();
            int temp = amount;
            int count = 0;

            for (int c : coins) {
                while (temp >= c) {
                    used.add(String.valueOf(c));
                    temp -= c;
                    count++;
                }
            }

            // Dinh dang gui len: "4; 10,5,2,1" hoac "<so luong>; <cac menh gia>"
            String result = count + "; " + String.join(",", used);
            System.out.println("Ket qua gui len: " + result);

            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
