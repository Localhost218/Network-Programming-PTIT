package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Char_Vigenere {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "vigenereCipher";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // Format: "Tu khoa;Chuoi dau vao"
            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Du lieu nhan: " + input);

            String[] parts = input.split(";");
            String key = parts[0].trim().toUpperCase();
            String text = parts[1].trim().toUpperCase();

            StringBuilder sb = new StringBuilder();
            int keyLen = key.length();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (Character.isLetter(c)) {
                    int shift = key.charAt(i % keyLen) - 'A';
                    char encrypted = (char) ('A' + (c - 'A' + shift) % 26);
                    sb.append(encrypted);
                } else {
                    sb.append(c);
                }
            }

            String result = sb.toString();
            System.out.println("Chuoi ma hoa Vigenere: " + result);
            service.submitCharacter(studentCode, qCode, result);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
