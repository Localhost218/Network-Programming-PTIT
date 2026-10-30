package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_Data_Combinations {
    private static void backtrack(List<Integer> arr, int k, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < arr.size(); i++) {
            current.add(arr.get(i));
            backtrack(arr, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "dataCombinations";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // Nhan dang: "[1, 2, 3, 4]; 2" hoac "1, 2, 3, 4; 2"
            String resp = (String) service.requestData(studentCode, qCode);
            System.out.println("Du lieu nhan: " + resp);

            String[] parts = resp.split(";");
            int k = Integer.parseInt(parts[1].trim());

            String arrPart = parts[0].replaceAll("[\[\]]", "").trim();
            String[] tokens = arrPart.split("[,\s]+");
            List<Integer> arr = new ArrayList<>();
            for (String t : tokens) {
                if (!t.isEmpty()) arr.add(Integer.parseInt(t.trim()));
            }

            List<List<Integer>> result = new ArrayList<>();
            backtrack(arr, k, 0, new ArrayList<>(), result);

            System.out.println("Danh sach to hop: " + result);
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
