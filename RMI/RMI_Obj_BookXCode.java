package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Obj_BookXCode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "bookXCode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            BookX book = (BookX) service.requestObject(studentCode, qCode);
            System.out.println("BookX: " + book.getTitle() + " by " + book.getAuthor());

            // 1. Chu cai dau va cuoi trong ten tac gia (author) in hoa
            String author = book.getAuthor().trim();
            char firstAuthor = Character.toUpperCase(author.charAt(0));
            char lastAuthor = Character.toUpperCase(author.charAt(author.length() - 1));

            // 2. Hai chu so cuoi cua nam xuat ban
            String yearStr = String.valueOf(book.getYearPublished());
            String yearCode = yearStr.length() >= 2 ? yearStr.substring(yearStr.length() - 2) : yearStr;

            // 3. So luong chu cai trong genre cua sach
            int genreLetterCount = 0;
            for (char c : book.getGenre().toCharArray()) {
                if (Character.isLetter(c)) genreLetterCount++;
            }

            // 4. Do dai cua title chia lay du cho 10
            int titleMod10 = book.getTitle().length() % 10;

            String code = "" + firstAuthor + lastAuthor + yearCode + genreLetterCount + titleMod10;
            System.out.println("Generated code: " + code);

            book.setCode(code);
            service.submitObject(studentCode, qCode, book);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
