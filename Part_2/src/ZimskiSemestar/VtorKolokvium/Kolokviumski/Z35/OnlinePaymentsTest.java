//package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z35;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//class Student {
//    private String index;
//    private Map<String, Integer> itemNames;
//
//    public Student(String line) {
//        itemNames = new HashMap<>();
//        String[] parts = line.split(";");
//        index = parts[0];
//        itemNames.put(parts[1], Integer.parseInt(parts[2]));
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Student: %s Net: %d Fee: %d Total: %d",
//                index, price, price, price);
//    }
//}
//
//class OnlinePayments {
//    List<Student> students;
//
//    public OnlinePayments() {
//        students = new ArrayList<>();
//    }
//
//    public void readItems(InputStream in) {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//        students = bufferedReader.lines().map(Student::new).collect(Collectors.toList());
//    }
//
//    public void printStudentReport(String index, OutputStream os) {
//
//    }
//}
//
//public class OnlinePaymentsTest {
//    public static void main(String[] args) {
//        OnlinePayments onlinePayments = new OnlinePayments();
//
//        onlinePayments.readItems(System.in);
//
//        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
//    }
//}
