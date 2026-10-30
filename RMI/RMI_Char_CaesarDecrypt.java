package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Char_CaesarDecrypt {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "caesarDecrypt";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuoi ma hoa: " + input);

            // Giai ma: do dich la input.length() % 7
            int shift = input.length() % 7;
            StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    sb.append((char) ('A' + (c - 'A' - shift + 26) % 26));
                } else if (Character.isLowerCase(c)) {
                    sb.append((char) ('a' + (c - 'a' - shift + 26) % 26));
                } else {
                    sb.append((char) (c - shift));
                }
            }

            String decrypted = sb.toString();
            System.out.println("Chuoi sau khi giai ma: " + decrypted);
            service.submitCharacter(studentCode, qCode, decrypted);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
