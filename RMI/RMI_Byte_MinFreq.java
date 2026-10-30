package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMI_Byte_MinFreq {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "byteMinFreq";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            byte[] data = service.requestData(studentCode, qCode);
            if (data == null || data.length == 0) return;

            Map<Byte, Integer> countMap = new LinkedHashMap<>();
            for (byte b : data) {
                countMap.put(b, countMap.getOrDefault(b, 0) + 1);
            }

            byte minElem = data[0];
            int minCount = Integer.MAX_VALUE;
            for (Map.Entry<Byte, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() < minCount) {
                    minCount = entry.getValue();
                    minElem = entry.getKey();
                }
            }

            byte[] result = new byte[] { minElem, (byte) minCount };
            service.submitData(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
