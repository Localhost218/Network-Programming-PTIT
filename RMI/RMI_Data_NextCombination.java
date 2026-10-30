package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RMI_Data_NextCombination {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "nextPermutation";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            DataService service = (DataService) registry.lookup("RMIDataService");

            String resp = (String) service.requestData(studentCode, qCode);
            System.out.println("Chuoi ban dau: " + resp);

            String[] tokens = resp.split("[,\s]+");
            int[] nums = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                nums[i] = Integer.parseInt(tokens[i].trim());
            }

            // Thuat toan sinh hoan vi ke tiep
            int i = nums.length - 2;
            while (i >= 0 && nums[i] >= nums[i + 1]) {
                i--;
            }

            if (i >= 0) {
                int j = nums.length - 1;
                while (nums[j] <= nums[i]) {
                    j--;
                }
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }

            // Dao nguoc tu i + 1 den het
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
                right--;
            }

            List<String> resStr = new ArrayList<>();
            for (int num : nums) resStr.add(String.valueOf(num));
            String result = String.join(", ", resStr);

            System.out.println("Hoan vi ke tiep: " + result);
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
