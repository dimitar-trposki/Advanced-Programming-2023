package Juni.VtorKolokvium.Kolokviumski.Z23;

import java.util.*;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public Integer yearOfStudies() {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    public double sumPoints() {
        return points.stream().mapToDouble(Integer::doubleValue).sum() / 10;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, points.size() >= 8 ? "YES" : "NO", sumPoints());
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::sumPoints)
                .thenComparing(Student::getIndex);
        return comparator.compare(this, o);
    }
}

class LabExercises {

    Set<Student> students;

    public LabExercises() {
        students = new TreeSet<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        students.stream()
                .sorted(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder())
                .limit(n)
                .forEach(System.out::println);

    }

    public List<Student> failedStudents() {
        return students.stream()
                .filter(s -> s.getPoints().size() < 8)
                .sorted(Comparator.comparing(Student::getIndex))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return students.stream()
                .filter(s -> s.getPoints().size() >= 8)
                .collect(Collectors.groupingBy(
                        Student::yearOfStudies,
                        Collectors.averagingDouble(Student::sumPoints)
                ));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}