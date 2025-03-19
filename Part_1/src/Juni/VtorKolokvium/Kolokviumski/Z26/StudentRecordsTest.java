package Juni.VtorKolokvium.Kolokviumski.Z26;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {
    String code;
    String major;
    List<Integer> grades;

    public Student(String code, String major, List<Integer> grades) {
        this.code = code;
        this.major = major;
        this.grades = grades;
    }

    public Student(String line) {
        String[] parts = line.split(" ");
        this.code = parts[0];
        this.major = parts[1];
        grades = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            grades.add(i - 2, Integer.parseInt(parts[i]));
        }
    }

    public double average() {
        return (double) grades.stream()
                .mapToInt(i -> i)
                .sum() / grades.size();
    }

    public String getCode() {
        return code;
    }

    public String getMajor() {
        return major;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, average());
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::average).reversed()
                .thenComparing(Student::getCode);
        return comparator.compare(this, o);
    }
}

class DirectionStatistics {
    private String direction;
    private Map<Integer, Integer> gradeCounts;

    public DirectionStatistics(String direction, Map<Integer, Integer> gradeCounts) {
        this.direction = direction;
        this.gradeCounts = gradeCounts;
    }

    public String getDirection() {
        return direction;
    }

    public int getTensCount() {
        return gradeCounts.get(10);
    }

    public Set<Integer> getGrades() {
        return gradeCounts.keySet();
    }

    public int getCount(int grade) {
        return gradeCounts.get(grade);
    }
}

class StudentRecords {
    Map<String, TreeSet<Student>> studentRecords;

    public StudentRecords() {
        studentRecords = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<Student> students = br.lines()
                .map(Student::new)
                .collect(Collectors.toList());

        for (Student student : students) {
            studentRecords.putIfAbsent(student.getMajor(), new TreeSet<>());
            studentRecords.get(student.getMajor()).add(student);
        }

        return students.size();
    }

    void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        studentRecords.forEach((k, v) -> {
            pw.println(k);
            v.forEach(pw::println);
        });

        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
        List<DirectionStatistics> statistics = new ArrayList<>();
        for (String direction : studentRecords.keySet()) {
            Map<Integer, Integer> gradeCounts = new TreeMap<>();
            for (int i = 6; i <= 10; i++) {
                gradeCounts.put(i, 0);
            }
            for (Student student : studentRecords.get(direction)) {
                for (int grade : student.getGrades()) {
                    gradeCounts.put(grade, gradeCounts.get(grade) + 1);
                }
            }
            statistics.add(new DirectionStatistics(direction, gradeCounts));
        }
        statistics.sort(Comparator.comparingInt(DirectionStatistics::getTensCount).reversed());

        for (DirectionStatistics stat : statistics) {
            writer.println(stat.getDirection());
            for (int grade : stat.getGrades()) {
                int count = stat.getCount(grade);
                writer.printf("%2d | %s(%d)%n", grade, "*".repeat(count % 10 == 0 ? count / 10 : (count / 10) + 1), count);
            }
        }
        writer.flush();
    }
}

public class StudentRecordsTest {
    public static void main(String[] args) throws IOException {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}