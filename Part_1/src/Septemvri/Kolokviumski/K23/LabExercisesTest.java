package Septemvri.Kolokviumski.K23;

import java.util.*;
import java.util.stream.Collectors;

class Student {

    String index;
    List<Integer> pointsFromLabs;
    static int NUM_OF_LAB_EXERCISES = 10;

    public Student(String index, List<Integer> pointsFromLabs) {
        this.index = index;
        this.pointsFromLabs = pointsFromLabs;
    }

    public String getIndex() {
        return index;
    }

    public int totalPoints() {
        return pointsFromLabs.stream().mapToInt(i -> i).sum();
    }

    public double avgPoints() {
        return (double) totalPoints() / (double) NUM_OF_LAB_EXERCISES;
    }

    public boolean hasSignature() {
        return pointsFromLabs.size() >= 8;
    }

    public int getGrade() {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, hasSignature() ? "YES" : "NO", avgPoints());
    }

}

class LabExercises {

    List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        Comparator<Student> comparator = Comparator.comparing(Student::avgPoints).thenComparing(Student::getIndex);
        if (!ascending) {
            comparator = comparator.reversed();
        }

        students.stream()
                .sorted(comparator)
                .limit(n)
                .forEach(System.out::println);
    }

    public List<Student> failedStudents() {
        return students.stream()
                .filter(s -> !s.hasSignature())
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::totalPoints))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return students.stream()
                .filter(Student::hasSignature)
                .collect(Collectors.groupingBy(Student::getGrade, Collectors.averagingDouble(Student::avgPoints)));
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