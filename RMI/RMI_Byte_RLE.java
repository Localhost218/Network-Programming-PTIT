package RMI;

import java.io.ByteArrayOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Byte_RLE {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "byteRLE";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte
            byte[] data = service.requestData(studentCode, qCode);
            if (data == null || data.length == 0) return;

            // b. Nen Run-Length Encoding: [byte, count]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int count = 1;
            for (int i = 1; i < data.length; i++) {
                if (data[i] == data[i - 1]) {
                    count++;
                } else {
                    baos.write(data[i - 1]);
                    baos.write((byte) count);
                    count = 1;
                }
            }
            baos.write(data[data.length - 1]);
            baos.write((byte) count);

            byte[] result = baos.toByteArray();

            // c. Gui ket qua ve server
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
