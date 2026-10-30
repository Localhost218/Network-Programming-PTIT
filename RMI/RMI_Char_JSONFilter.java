package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RMI_Char_JSONFilter {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "jsonFilter";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // Vi du: {"name": "Alice", "age": 25, "city": "Wonderland", "country": "Fictionland"}
            String json = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuoi JSON: " + json);

            // Tach cac cap key: value
            Pattern pattern = Pattern.compile(""([^"]+)"\s*:\s*([^,}]+)");
            Matcher matcher = pattern.matcher(json);

            List<String> pairs = new ArrayList<>();
            while (matcher.find()) {
                String key = matcher.group(1).trim();
                String value = matcher.group(2).trim().replaceAll("^"|"$", "");
                pairs.add(key + ": " + value);
            }

            // Vi tri chan (0, 2, ...) va le (1, 3, ...) theo 0-based
            List<String> evens = new ArrayList<>();
            List<String> odds = new ArrayList<>();
            for (int i = 0; i < pairs.size(); i++) {
                if (i % 2 == 0) {
                    evens.add(pairs.get(i));
                } else {
                    odds.add(pairs.get(i));
                }
            }

            String result = String.join(", ", evens) + "; " + String.join(", ", odds);
            System.out.println("Ket qua trich xuat: " + result);

            service.submitCharacter(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
