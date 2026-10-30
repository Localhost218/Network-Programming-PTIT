package RMI;

import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Byte_Hex {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "byteHex";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte
            byte[] data = service.requestData(studentCode, qCode);

            // b. Chuyen moi byte thanh chuoi hex 2 ky tu thuong
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {
                sb.append(String.format("%02x", b & 0xFF));
            }
            byte[] result = sb.toString().getBytes(StandardCharsets.US_ASCII);

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
