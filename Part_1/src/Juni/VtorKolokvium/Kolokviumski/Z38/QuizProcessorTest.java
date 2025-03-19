package Juni.VtorKolokvium.Kolokviumski.Z38;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InEqualNumberOfAnswers extends Exception {
    public InEqualNumberOfAnswers(String message) {
        super(message);
    }
}

class Quiz {
    private final String id;
    private final List<String> correctAnswers;
    private final List<String> studentAnswers;
    private double points = 0;

    public Quiz(String line) throws InEqualNumberOfAnswers {
        String[] parts = line.split(";");
        id = parts[0];
        correctAnswers = Arrays.stream(parts[1].split(", ")).collect(Collectors.toList());
        studentAnswers = Arrays.stream(parts[2].split(", ")).collect(Collectors.toList());
        numOfPoints();
    }

    public void numOfPoints() throws InEqualNumberOfAnswers {
        if (correctAnswers.size() != studentAnswers.size()) {
            throw new InEqualNumberOfAnswers("A quiz must have same number of correct and selected answers");
        }
        IntStream.range(0, correctAnswers.size()).forEach(i -> {
            if (correctAnswers.get(i).equals(studentAnswers.get(i))) {
                points++;
            } else {
                points -= 0.25;
            }
        });
    }

    public String getId() {
        return id;
    }

    public double getPoints() {
        return points;
    }
}

class QuizProcessor {
    public static Map<String, Double> processAnswers(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.lines().map(line -> {
                    try {
                        return new Quiz(line);
                    } catch (InEqualNumberOfAnswers e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getId, Quiz::getPoints, Double::sum, TreeMap::new));

    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}