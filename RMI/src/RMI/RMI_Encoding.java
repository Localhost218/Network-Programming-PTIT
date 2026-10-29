/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;

/**
 *
 * @author ngtukien
 */
import java.net.URI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Encoding {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "Ph1BjWuP";

        try {
            // 1. Ket noi RMI Registry
            Registry registry = LocateRegistry.getRegistry(host);

            CharacterService service =
                    (CharacterService) registry.lookup(
                            "RMICharacterService");

            // 2. Nhan chuoi tu server
            String input =
                    service.requestCharacter(studentCode, qCode);

            System.out.println("Chuoi ban dau: " + input);

            // 3. URL encode theo quy tac URI path
            String encoded = new URI(
                    null, null, input, null
            ).toASCIIString();

            System.out.println("Chuoi da ma hoa: " + encoded);

            // 4. Gui ket qua ve server
            service.submitCharacter(
                    studentCode, qCode, encoded);

            System.out.println("Submit thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}