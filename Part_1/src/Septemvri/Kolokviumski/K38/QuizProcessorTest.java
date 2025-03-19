package Septemvri.Kolokviumski.K38;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class DifferentNumberOfAnswersException extends Exception {

    public DifferentNumberOfAnswersException(String message) {
        super(message);
    }

}

class Student {

    String ID;
    List<String> correctAnswers;
    List<String> studentAnswers;
    double points;

    public Student(String line) throws DifferentNumberOfAnswersException {
        String[] parts = line.split(";");
        this.ID = parts[0];
        this.correctAnswers = Arrays.stream(parts[1].split(", ")).collect(Collectors.toList());
        this.studentAnswers = Arrays.stream(parts[2].split(", ")).collect(Collectors.toList());
        totalPoints();
    }

    public String getID() {
        return ID;
    }

    public double getPoints() {
        return points;
    }

    public void totalPoints() throws DifferentNumberOfAnswersException {
        if (correctAnswers.size() != studentAnswers.size()) {
            throw new DifferentNumberOfAnswersException("A quiz must have same number of correct and selected answers");
        }
        for (int i = 0; i < correctAnswers.size(); i++) {
            if (correctAnswers.get(i).equals(studentAnswers.get(i))) {
                points++;
            } else {
                points -= 0.25;
            }
        }
    }

}

class QuizProcessor {

    public static Map<String, Double> processAnswers(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        return br.lines().map(line -> {
                    try {
                        return new Student(line);
                    } catch (DifferentNumberOfAnswersException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Student::getID, Student::getPoints, Double::sum, LinkedHashMap::new));
    }

}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}