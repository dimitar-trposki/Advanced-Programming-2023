package Septemvri.Kolokviumski.K31;

import java.util.*;
import java.util.stream.Collectors;

class InvalidNumberOfPointsException extends Exception {

    public InvalidNumberOfPointsException() {
    }

}

class Student {

    String index;
    String name;
    int pointsFirstMidTerm;
    int pointsSecondMidTerm;
    int pointsLabExercises;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public double summaryPoints() {
        return pointsFirstMidTerm * 0.45 + pointsSecondMidTerm * 0.45 + pointsLabExercises;
    }

    public int grade() {
        if (summaryPoints() < 50) {
            return 5;
        } else if (summaryPoints() >= 50 && summaryPoints() < 60) {
            return 6;
        } else if (summaryPoints() >= 60 && summaryPoints() < 70) {
            return 7;
        } else if (summaryPoints() >= 70 && summaryPoints() < 80) {
            return 8;
        } else if (summaryPoints() >= 80 && summaryPoints() < 90) {
            return 9;
        } else {
            return 10;
        }
    }

    public void updateStudent(String activity, int points) throws InvalidNumberOfPointsException {
        if (activity.equals("midterm1")) {
            if (points > 100 || points < 0) {
                throw new InvalidNumberOfPointsException();
            }
            pointsFirstMidTerm += points;
        } else if (activity.equals("midterm2")) {
            if (points > 100 || points < 0) {
                throw new InvalidNumberOfPointsException();
            }
            pointsSecondMidTerm += points;
        } else if (activity.equals("labs")) {
            if (points > 10 || points < 0) {
                throw new InvalidNumberOfPointsException();
            }
            pointsLabExercises += points;
        }
    }

    @Override
    public String toString() {
        String sb = "ID: " + index +
                " Name: " + name +
                " First midterm: " + pointsFirstMidTerm +
                " Second midterm " + pointsSecondMidTerm +
                " Labs: " + pointsLabExercises +
                String.format(" Summary points: %.2f", summaryPoints()) +
                " Grade: " + grade();
        return sb;
    }

}

class AdvancedProgrammingCourse {

    Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }

    public void addStudent(Student s) {
        students.put(s.getIndex(), s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        Student s = students.get(idNumber);
        try {
            s.updateStudent(activity, points);
        } catch (InvalidNumberOfPointsException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return students.values().stream()
                .sorted(Comparator.comparing(Student::summaryPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> grades = new TreeMap<>();
        for (int i = 5; i < 11; i++) {
            grades.putIfAbsent(i, 0);
        }
        for (Student value : students.values()) {
            grades.put(value.grade(), grades.get(value.grade()) + 1);
        }
        return grades;
    }

    public void printStatistics() {
        DoubleSummaryStatistics dss = students.values().stream()
                .mapToDouble(Student::summaryPoints)
                .filter(p -> p >= 50)
                .summaryStatistics();
        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f%n",
                dss.getCount(),
                dss.getMin(),
                dss.getAverage(),
                dss.getMax());
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