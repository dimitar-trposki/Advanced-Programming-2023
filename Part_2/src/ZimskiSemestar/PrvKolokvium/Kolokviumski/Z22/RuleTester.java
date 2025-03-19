package ZimskiSemestar.PrvKolokvium.Kolokviumski.Z22;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

class Rule<E, T> {
    private Predicate<E> predicate;
    private Function<E, T> function;

    public Rule(Predicate<E> predicate, Function<E, T> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public Optional<T> apply(E input) {
        if (predicate.test(input)) {
            return Optional.of(function.apply(input));
        } else {
            return Optional.empty();
        }
    }
}

class RuleProcessor {
    public static <IN, OUT> void process(List<IN> input, List<Rule<IN, OUT>> rules) {
        for (IN in : input) {
            System.out.println("Input: " + in.toString());
            for (Rule<IN, OUT> rule : rules) {
                Optional<OUT> optional = rule.apply(in);
                if (optional.isPresent()) {
                    System.out.println("Result: " + optional.get());
                } else {
                    System.out.println("Condition not met");
                }
            }
        }
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            rules.add(new Rule<>(r -> r.contains("NP"), r -> r.indexOf("NP")));

            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            rules.add(new Rule<>(r -> r.startsWith("NP"), r -> r.length()));

            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            rules.add(new Rule<>(s -> s.grades.size() >= 3, s -> s.grades.stream().mapToDouble(g -> g).max().getAsDouble()));

            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0
            rules.add(new Rule<>(s -> s.id.startsWith("20"), s -> s.grades.stream().mapToDouble(g -> g).average().orElse(5.0)));

            List<Student> students = new ArrayList<>();
            while (sc.hasNext()) {
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}
