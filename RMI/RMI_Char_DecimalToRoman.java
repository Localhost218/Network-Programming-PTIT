package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Char_DecimalToRoman {
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static String toRoman(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VALUES.length; i++) {
            while (num >= VALUES[i]) {
                num -= VALUES[i];
                sb.append(SYMBOLS[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "decimalToRoman";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("So thap phan nhan duoc: " + input);

            int num = Integer.parseInt(input.trim());
            String roman = toRoman(num);
            System.out.println("Chuoi so La Ma: " + roman);

            service.submitCharacter(studentCode, qCode, roman);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
