package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Obj_BookCode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "bookCode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            Book book = (Book) service.requestObject(studentCode, qCode);
            System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());

            // 1. Chu cai dau cua ho va ten dau tac gia (vi du: "Jane Austen" -> "JA")
            String[] authorWords = book.getAuthor().trim().split("\s+");
            String authorInitial = "";
            if (authorWords.length >= 2) {
                authorInitial = "" + authorWords[0].toUpperCase().charAt(0) + authorWords[authorWords.length - 1].toUpperCase().charAt(0);
            } else if (authorWords.length == 1) {
                authorInitial = "" + authorWords[0].toUpperCase().charAt(0);
            }

            // 2. Hai chu so cuoi cua nam xuat ban
            String yearStr = String.valueOf(book.getYearPublished());
            String yearCode = yearStr.length() >= 2 ? yearStr.substring(yearStr.length() - 2) : yearStr;

            // 3. Tong so chu cai trong title cua sach
            int titleLetterCount = 0;
            for (char c : book.getTitle().toCharArray()) {
                if (Character.isLetter(c)) titleLetterCount++;
            }

            // 4. So trang dinh dang 3 chu so (neu < 3 them 0 o dau)
            String pageCode = String.format("%03d", book.getPageCount());

            // Ma code: [authorInitial][yearCode][titleLetterCount][pageCode]
            String code = authorInitial + yearCode + titleLetterCount + pageCode;
            System.out.println("Generated code: " + code);

            book.setCode(code);
            service.submitObject(studentCode, qCode, book);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
