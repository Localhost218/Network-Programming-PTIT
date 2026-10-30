package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMI_Byte_MaxFreq {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "byteMaxFreq";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            byte[] data = service.requestData(studentCode, qCode);
            if (data == null || data.length == 0) return;

            Map<Byte, Integer> countMap = new LinkedHashMap<>();
            for (byte b : data) {
                countMap.put(b, countMap.getOrDefault(b, 0) + 1);
            }

            byte maxElem = data[0];
            int maxCount = -1;
            for (Map.Entry<Byte, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxElem = entry.getKey();
                }
            }

            byte[] result = new byte[] { maxElem, (byte) maxCount };
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
