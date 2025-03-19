package IspitSeptemvri.Kolokviumski.K26;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {

    String id;
    List<Integer> grades;

    public Student(String line) {
        String[] parts = line.split("\\s+");
        this.id = parts[0];
        this.grades = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            grades.add(Integer.parseInt(parts[i]));
        }
    }

    public String getId() {
        return id;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double averageGrade() {
        return (double) grades.stream().mapToInt(i -> i).sum() / (double) grades.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::averageGrade).reversed().thenComparing(Student::getId);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", id, averageGrade());
    }

}

class GradesPerMajor implements Comparable<GradesPerMajor> {

    String majorName;
    Map<Integer, Integer> gradeDistribution;

    public GradesPerMajor(String line) {
        String[] parts = line.split("\\s+");
        this.majorName = parts[1];
        this.gradeDistribution = new TreeMap<>();
        this.gradeDistribution.putIfAbsent(6, 0);
        this.gradeDistribution.putIfAbsent(7, 0);
        this.gradeDistribution.putIfAbsent(8, 0);
        this.gradeDistribution.putIfAbsent(9, 0);
        this.gradeDistribution.putIfAbsent(10, 0);
    }

    public void countGrades(String line) {
        String[] parts = line.split("\\s+");
        for (int i = 2; i < parts.length; i++) {
            int grade = Integer.parseInt(parts[i]);
            gradeDistribution.computeIfPresent(grade, (k, v) -> ++v);
        }
    }

    @Override
    public int compareTo(GradesPerMajor o) {
        return Integer.compare(o.gradeDistribution.get(10), this.gradeDistribution.get(10));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(majorName).append("\n");
        gradeDistribution.entrySet().stream().forEach(entry -> {
            stringBuilder.append(String.format("%2d | %s(%d)"
                    , entry.getKey(), "*".repeat(entry.getValue() % 10 == 0 ? entry.getValue() / 10 : (entry.getValue() / 10) + 1), entry.getValue())).append("\n");
        });
        return stringBuilder.toString();
    }

}

class StudentRecords {

    Map<String, Set<Student>> studentsPerMajor;
    Map<String, GradesPerMajor> grades;

    public StudentRecords() {
        this.studentsPerMajor = new TreeMap<>();
        this.grades = new TreeMap<>();
    }

    public int readRecords(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int counter = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            studentsPerMajor.putIfAbsent(parts[1], new TreeSet<>());
            studentsPerMajor.get(parts[1]).add(new Student(line));
            counter++;

            grades.putIfAbsent(parts[1], new GradesPerMajor(line));
            grades.get(parts[1]).countGrades(line);
        }
        return counter;
    }

    public void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        studentsPerMajor.entrySet().forEach(entry -> {
            pw.println(entry.getKey());
            entry.getValue().stream().forEach(pw::println);
        });
        pw.flush();
    }

    public void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        List<Map.Entry<String, GradesPerMajor>> entryList = new ArrayList<>(grades.entrySet());
        entryList.sort(Map.Entry.comparingByValue());

        grades = entryList.stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        grades.entrySet().forEach(entry -> pw.print(entry.getValue()));

        pw.flush();
    }

}

public class StudentRecordsTest {
    public static void main(String[] args) {
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