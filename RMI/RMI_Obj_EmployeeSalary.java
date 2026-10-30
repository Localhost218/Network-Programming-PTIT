package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Obj_EmployeeSalary {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        String studentCode = "B23DCCN465";
        String qCode = "empSalary";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            Employee emp = (Employee) service.requestObject(studentCode, qCode);
            System.out.println("Employee: " + emp.getName() + ", BaseSalary: " + emp.getBaseSalary() + ", ExpYears: " + emp.getExperienceYears());

            int years = emp.getExperienceYears();

            // Tong cac chu so cua experienceYears
            int sumDigits = 0;
            int temp = Math.abs(years);
            while (temp > 0) {
                sumDigits += temp % 10;
                temp /= 10;
            }

            // So luong uoc so nguyen cua experienceYears
            int countDivisors = 0;
            for (int i = 1; i <= Math.abs(years); i++) {
                if (years % i == 0) countDivisors++;
            }

            // factor = (So nam lam viec + tong chu so + so luong uoc so) / 100.0
            double factor = (years + sumDigits + countDivisors) / 100.0;
            double finalSalary = emp.getBaseSalary() * (1 + factor);

            emp.setFinalSalary(finalSalary);
            System.out.println("Factor: " + factor + ", FinalSalary: " + finalSalary);

            service.submitObject(studentCode, qCode, emp);
            System.out.println("Submit thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
