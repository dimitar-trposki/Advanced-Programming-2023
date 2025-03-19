package Juni.VtorKolokvium.Kolokviumski.Z31;

import java.util.*;
import java.util.stream.Collectors;

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
    }
}

class Student implements Comparable<Student> {
    String index;
    String name;
    int pointFirstMidterm;
    int pointSecondMidterm;
    int pointsLabEx;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.pointFirstMidterm = 0;
        this.pointSecondMidterm = 0;
        this.pointsLabEx = 0;
    }

    public void updateStudent(String activity, int points) throws InvalidNumberException {
        if (activity.equals("midterm1")) {
            if (points > 100) {
                throw new InvalidNumberException();
            }
            pointFirstMidterm = points;
        } else if (activity.equals("midterm2")) {
            if (points > 100) {
                throw new InvalidNumberException();
            }
            pointSecondMidterm = points;
        } else {
            if (points > 10) {
                throw new InvalidNumberException();
            }
            pointsLabEx = points;
        }
    }

    public double summaryPoints() {
        return pointFirstMidterm * 0.45 + pointSecondMidterm * 0.45 + pointsLabEx;
    }

    public int grade() {
        int grade = ((int) summaryPoints() / 10) + 1;
        if (grade < 5) {
            grade = 5;
        } else if (grade > 10) {
            grade = 10;
        }
        return grade;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                index, name, pointFirstMidterm, pointSecondMidterm, pointsLabEx, summaryPoints(), grade());
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::summaryPoints).reversed();
        return comparator.compare(this, o);
    }
}

class AdvancedProgrammingCourse {
    Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new TreeMap<>();
    }

    public void addStudent(Student s) {
        students.put(s.index, s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        try {
            students.get(idNumber).updateStudent(activity, points);
        } catch (InvalidNumberException e) {
            e.getMessage();
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return students.values().stream()
                .sorted()
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> gradeDistribution = new TreeMap<>();
        for (int i = 5; i < 11; i++) {
            int gradeValue = i;
            gradeDistribution.putIfAbsent(i,
                    (students.values().stream()
                            .filter(s -> s.grade() == gradeValue)
                            .mapToInt(g -> g.grade())
                            .sum()) / i);
        }
        return gradeDistribution;
    }

    public void printStatistics() {
        DoubleSummaryStatistics dss = students.values().stream()
                .filter(s -> s.grade() >= 6)
                .mapToDouble(Student::summaryPoints)
                .summaryStatistics();
        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f%n",
                dss.getCount(), dss.getMin(), dss.getAverage(), dss.getMax());
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

