/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;

/**
 *
 * @author ngtukien
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Roman {

    // Chuyen so nguyen duong sang so La Ma
    public static String toRoman(int number) {
        int[] values = {
            1000, 900, 500, 400, 100, 90,
            50, 40, 10, 9, 5, 4, 1
        };

        String[] symbols = {
            "M", "CM", "D", "CD", "C", "XC",
            "L", "XL", "X", "IX", "V", "IV", "I"
        };

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                result.append(symbols[i]);
                number -= values[i];
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "VvOWmiT8";

        try {
            // 1. Ket noi RMI Registry
            Registry registry = LocateRegistry.getRegistry(host);

            CharacterService service =
                    (CharacterService) registry.lookup(
                            "RMICharacterService");

            // 2. Nhan chuoi so tu server
            String input =
                    service.requestCharacter(studentCode, qCode);

            System.out.println("So nhan duoc: " + input);

            // 3. Chuyen sang so La Ma
            int number = Integer.parseInt(input.trim());
            String roman = toRoman(number);

            System.out.println("So La Ma: " + roman);

            // 4. Gui ket qua ve server
            service.submitCharacter(studentCode, qCode, roman);

            System.out.println("Submit thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
