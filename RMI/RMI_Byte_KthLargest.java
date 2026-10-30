package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class RMI_Byte_KthLargest {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "kthLargestByte";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte, phan tu cuoi la K
            byte[] data = service.requestData(studentCode, qCode);
            if (data == null || data.length < 2) return;

            int k = data[data.length - 1];
            byte[] sub = Arrays.copyOf(data, data.length - 1);
            Arrays.sort(sub); // Tang dan

            byte kthLargest = sub[sub.length - k];
            byte[] result = new byte[] { kthLargest, (byte) k };

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
