package Septemvri.Kolokviumski.K35;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {

    String index;
    Map<String, Integer> payments;
    static final double PROVISION = 1.14;

    public Student(String line) {
        String[] parts = line.split(";");
        this.index = parts[0];
        this.payments = new HashMap<>();
    }

    public void addItem(String line) {
        String[] parts = line.split(";");
        this.payments.put(parts[1], Integer.parseInt(parts[2]));
    }

    private int net() {
        return payments.values().stream().mapToInt(i -> i).sum();
    }

    private int fee() {
        int feeToReturn = (int) Math.round(net() * (PROVISION / 100));
        if (feeToReturn < 3) {
            feeToReturn = 3;
        } else if (feeToReturn > 300) {
            feeToReturn = 300;
        }
        return feeToReturn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(index);
        sb.append(" Net: ").append(net());
        sb.append(" Fee: ").append(fee());
        sb.append(" Total: ").append(net() + fee()).append("\n");
        sb.append("Items:\n");
        int i = 1;
        this.payments = this.payments.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // merge function for duplicate keys
                        LinkedHashMap::new));
        for (Map.Entry<String, Integer> entry : payments.entrySet()) {
            sb.append(String.format("%d. %s %d\n", i++, entry.getKey(), entry.getValue()));
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        return sb.toString();
    }

}

class OnlinePayments {

    Map<String, Student> students;

    public OnlinePayments() {
        this.students = new TreeMap<>();
    }

    public void readItems(InputStream is) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        bufferedReader.lines().forEach(line -> {
            String[] parts = line.split(";");
            students.putIfAbsent(parts[0], new Student(line));
            students.get(parts[0]).addItem(line);
        });
    }

    public void printStudentReport(String index, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        if (students.containsKey(index)) {
            pw.println(students.get(index));
        } else {
            pw.println(String.format("Student %s not found!", index));
        }

        pw.flush();
    }

}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}