package Juni.VtorKolokvium.Kolokviumski.Z35;

import java.io.*;
import java.rmi.StubNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Item {
    String index;
    String name;
    int price;

    public Item(String line) {
        String[] parts = line.split(";");
        this.index = parts[0];
        this.name = parts[1];
        this.price = Integer.parseInt(parts[2]);
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s %d", name, price);
    }
}

class Student {
    String index;
    List<Item> items;
    static double FEE = 1.14;

    public Student(String index) {
        this.index = index;
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public int net() {
        return items.stream().mapToInt(i -> i.price).sum();
    }

    public int fee() {
        int fee = (int) Math.round((FEE / 100) * net());
        if (fee < 3) {
            return 3;
        } else if (fee > 300) {
            return 300;
        }
        return fee;
    }

    public int total() {
        return net() + fee();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s Net: %d Fee: %d Total: %d", index, net(), fee(), total()));
        sb.append("\nItems:\n");
        items = items.stream().sorted(Comparator.comparing(Item::getPrice).reversed())
                .collect(Collectors.toList());
        sb.append(1).append(". ").append(items.get(0).toString());
        for (int i = 1; i < items.size(); i++) {
            sb.append("\n").append(i + 1).append(". ").append(items.get(i).toString());
        }
        return sb.toString();
    }
}

class OnlinePayments {
    Map<String, Student> students;

    public OnlinePayments() {
        students = new HashMap<>();
    }

    public void readItems(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.lines()
                .map(Item::new)
                .forEach(student -> {
                    students.putIfAbsent(student.index, new Student(student.index));
                    students.get(student.index).addItem(student);
                });
    }

    public void printStudentReport(String index, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        Student s = students.get(index);
        if (s == null) {
            pw.println("Student " + index + " not found!");
        } else {
            pw.println(s);
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
