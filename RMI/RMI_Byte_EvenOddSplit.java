package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Byte_EvenOddSplit {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "evenOddSplit";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte
            byte[] data = service.requestData(studentCode, qCode);

            // b. Phan chia byte chan truoc, le sau (giu nguyen thu tu xuat hien)
            byte[] result = new byte[data.length];
            int idx = 0;
            for (byte b : data) {
                if (b % 2 == 0) result[idx++] = b;
            }
            for (byte b : data) {
                if (b % 2 != 0) result[idx++] = b;
            }

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
