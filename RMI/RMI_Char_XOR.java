package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Char_XOR {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "charXor";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // Format: "Khoa XOR;Chuoi dau vao"
            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Du lieu nhan: " + input);

            String[] parts = input.split(";");
            String key = parts[0].trim();
            String text = parts[1].trim();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                char k = key.charAt(i % key.length());
                sb.append((char) (c ^ k));
            }

            String result = sb.toString();
            System.out.println("Chuoi ma hoa XOR: " + result);
            service.submitCharacter(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
