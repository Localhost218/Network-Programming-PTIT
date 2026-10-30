package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMI_Char_CountAndFormat {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "charCountFormat";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // Vi du: "AAABBC" -> "A3B2C1"
            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuoi dau vao: " + input);

            Map<Character, Integer> map = new LinkedHashMap<>();
            for (char c : input.toCharArray()) {
                map.put(c, map.getOrDefault(c, 0) + 1);
            }

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                sb.append(entry.getKey()).append(entry.getValue());
            }

            String result = sb.toString();
            System.out.println("Ket qua format <Ky tu><So lan>: " + result);

            service.submitCharacter(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
