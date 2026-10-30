package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Obj_StudentCode {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "studentCode";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            Student student = (Student) service.requestObject(studentCode, qCode);
            System.out.println("Student: " + student.getName() + ", Enrollment: " + student.getEnrollmentYear());

            // 1. Chuan hoa ten: Title Case
            String[] words = student.getName().trim().split("\s+");
            StringBuilder normalizedName = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                String w = words[i];
                if (!w.isEmpty()) {
                    String normWord = Character.toUpperCase(w.charAt(0)) + w.substring(1).toLowerCase();
                    normalizedName.append(normWord);
                    if (i < words.length - 1) normalizedName.append(" ");
                }
            }
            String fullName = normalizedName.toString();
            student.setName(fullName);

            // 2. Tao ma code cho sinh vien:
            // "B" + 2 so cuoi enrollmentYear + TEN in hoa + "_" + chu cai dau Ho & Ten lot in hoa
            // Vi du: "Nguyen Van Tuan", 2022 -> "B22TUAN_NV"
            String yearStr = String.valueOf(student.getEnrollmentYear());
            String yearCode = yearStr.length() >= 2 ? yearStr.substring(yearStr.length() - 2) : yearStr;

            String firstName = words[words.length - 1].toUpperCase();
            StringBuilder initials = new StringBuilder();
            for (int i = 0; i < words.length - 1; i++) {
                initials.append(Character.toUpperCase(words[i].charAt(0)));
            }

            String code = "B" + yearCode + firstName + "_" + initials.toString();
            System.out.println("Normalized name: " + fullName);
            System.out.println("Generated code: " + code);

            student.setCode(code);
            service.submitObject(studentCode, qCode, student);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
