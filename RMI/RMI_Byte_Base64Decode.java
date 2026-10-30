package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;

public class RMI_Byte_Base64Decode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "b64Decode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte chua chuoi Base64
            byte[] data = service.requestData(studentCode, qCode);

            // b. Giai ma Base64 ve byte goc
            byte[] decoded = Base64.getDecoder().decode(data);

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, decoded);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
