package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Byte_XOR {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "byteXor";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte
            byte[] data = service.requestData(studentCode, qCode);
            System.out.println("Du lieu nhan ve do dai: " + data.length);

            // b. Ma hoa XOR voi khoa "PTIT"
            byte[] key = "PTIT".getBytes();
            byte[] result = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                result[i] = (byte) (data[i] ^ key[i % key.length]);
            }

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
