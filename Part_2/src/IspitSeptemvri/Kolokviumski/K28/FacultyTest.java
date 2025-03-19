package IspitSeptemvri.Kolokviumski.K28;

import java.util.*;
import java.util.stream.Collectors;

class OperationNotAllowedException extends Exception {

    public OperationNotAllowedException(String message) {
        super(message);
    }

}

class Course {

    String name;
    int grade;

    public Course(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return grade == course.grade && Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, grade);
    }

}

class CourseStatistics implements Comparable<CourseStatistics> {

    String name;
    int numberOfParticipants;
    List<Integer> grades;

    public CourseStatistics(String name) {
        this.name = name;
        this.numberOfParticipants = 0;
        this.grades = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void increment() {
        ++numberOfParticipants;
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public double averageGrade() {
        return grades.stream()
                .mapToDouble(i -> i)
                .summaryStatistics()
                .getAverage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseStatistics that = (CourseStatistics) o;
        return numberOfParticipants == that.numberOfParticipants && Objects.equals(name, that.name) && Objects.equals(grades, that.grades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, numberOfParticipants, grades);
    }

    @Override
    public int compareTo(CourseStatistics o) {
        Comparator<CourseStatistics> comparator = Comparator.comparing(CourseStatistics::getNumberOfParticipants).thenComparing(CourseStatistics::averageGrade).thenComparing(CourseStatistics::getName);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", name, numberOfParticipants, averageGrade());
    }

}

class Student implements Comparable<Student> {

    String id;
    int yearsOfStudies;
    Map<Integer, Set<Course>> gradesPerTerm;
    int totalGrades;
    boolean hasGraduated;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        this.gradesPerTerm = new TreeMap<>();
        this.totalGrades = 0;
        this.hasGraduated = false;
        for (int i = 1; i <= yearsOfStudies * 2; i++) {
            gradesPerTerm.putIfAbsent(i, new TreeSet<>(Comparator.comparing(Course::getName)));
        }
    }

    public String getId() {
        return id;
    }

    public int getTotalGrades() {
        return totalGrades;
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        if ((yearsOfStudies == 3 && term > 6) || (term < 1) || (yearsOfStudies == 4 && term > 8)) {
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, id));
        }
        if (gradesPerTerm.get(term).stream().count() == 3) {
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", id, term));
        }
        gradesPerTerm.get(term).add(new Course(courseName, grade));
        totalGrades++;
        if ((yearsOfStudies == 3 && totalGrades == 18) || (yearsOfStudies == 4 && totalGrades == 24)) {
            hasGraduated = true;
        }
    }

    public double averageGrade() {
        double average = 0;
        if (totalGrades == 0) {
            average = 5.00;
        } else {
            average = (double) gradesPerTerm.values().stream()
                    .flatMap(Collection::stream)
                    .mapToInt(Course::getGrade)
                    .sum() / (double) totalGrades;
        }
        return average;
    }

    public String detailedReport() {
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> courses = new TreeSet<>();

        stringBuilder.append("Student: ").append(id).append("\n");
        for (Map.Entry<Integer, Set<Course>> entry : gradesPerTerm.entrySet()) {
            stringBuilder.append("Term ").append(entry.getKey()).append("\n");
            stringBuilder.append("Courses: ").append(entry.getValue().stream().count()).append("\n");
            stringBuilder.append("Average grade for term: ");
            DoubleSummaryStatistics dss = entry.getValue().stream()
                    .mapToDouble(Course::getGrade)
                    .summaryStatistics();
            stringBuilder.append(String.format("%.2f", dss.getAverage() == 0 ? 5.00 : dss.getAverage())).append("\n");
            courses.addAll(entry.getValue().stream()
                    .map(Course::getName).collect(Collectors.toSet()));
        }
        stringBuilder.append(String.format("Average grade: %.2f", averageGrade())).append("\n");
        stringBuilder.append("Courses attended: ");
        stringBuilder.append(courses.stream().collect(Collectors.joining(",")));

        return stringBuilder.toString();
    }

    public String shortReport() {
        return String.format("Student: %s Courses passed: %s Average grade: %.2f",
                id, totalGrades, averageGrade());
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::getTotalGrades).thenComparing(Student::averageGrade).thenComparing(Student::getId).reversed();
        return comparator.compare(this, o);
    }

}

class Faculty {

    Map<String, Student> students;
    Map<String, CourseStatistics> courseStatistics;
    List<String> logs;

    public Faculty() {
        this.students = new TreeMap<>();
        this.logs = new ArrayList<>();
        this.courseStatistics = new TreeMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.put(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        courseStatistics.putIfAbsent(courseName, new CourseStatistics(courseName));
        courseStatistics.get(courseName).increment();
        courseStatistics.get(courseName).addGrade(grade);
        Student student = students.get(studentId);
        student.addGrade(term, courseName, grade);
        if (student.hasGraduated) {
            logs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years.",
                    student.id, student.averageGrade(), student.yearsOfStudies));
            students.remove(studentId);
        }
    }

    String getFacultyLogs() {
        return logs.stream().collect(Collectors.joining("\n"));
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).detailedReport();
    }

    void printFirstNStudents(int n) {
        students.values().stream()
                .sorted()
                .limit(n)
                .forEach(s -> System.out.println(s.shortReport()));
    }

    void printCourses() {
        Set<CourseStatistics> courseStatisticsSet = new TreeSet<>();
        courseStatisticsSet.addAll(courseStatistics.values());

        courseStatisticsSet.stream().forEach(System.out::println);
    }

}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}