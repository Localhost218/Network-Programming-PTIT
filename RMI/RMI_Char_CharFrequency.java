package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RMI_Char_CharFrequency {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "charFrequency";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuoi dau vao: " + input);

            Map<Character, Integer> freq = new LinkedHashMap<>();
            for (char c : input.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }

            // Dinh dang JSON: {"H": 1, "e": 1, ...}
            List<String> items = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
                items.add(""" + entry.getKey() + "": " + entry.getValue());
            }
            String result = "{" + String.join(", ", items) + "}";
            System.out.println("Ket qua tan so ky tu: " + result);

            service.submitCharacter(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
