package RMI;

import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;

public class RMI_Char_Base64Encode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "b64Encode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuoi dau vao: " + input);

            String encoded = Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
            System.out.println("Chuoi ma hoa Base64: " + encoded);

            service.submitCharacter(studentCode, qCode, encoded);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
