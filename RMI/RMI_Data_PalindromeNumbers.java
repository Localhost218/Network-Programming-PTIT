package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI_Data_PalindromeNumbers {
    public static boolean isPalindrome(int num) {
        String s = String.valueOf(num);
        String rev = new StringBuilder(s).reverse().toString();
        return s.equals(rev);
    }

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "palindromeNums";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // Nhan N va K (vi du: "50, 150" hoac "50; 150")
            String resp = (String) service.requestData(studentCode, qCode);
            System.out.println("Du lieu nhan: " + resp);

            String[] parts = resp.split("[,;\s]+");
            int n = Integer.parseInt(parts[0].trim());
            int k = Integer.parseInt(parts[1].trim());

            List<Integer> palindromes = new ArrayList<>();
            for (int i = n; i <= k; i++) {
                if (isPalindrome(i)) {
                    palindromes.add(i);
                }
            }

            System.out.println("Cac so doi xung trong khoang [" + n + ", " + k + "]: " + palindromes);
            service.submitData(studentCode, qCode, palindromes);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
