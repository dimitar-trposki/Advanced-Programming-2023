package Juni.VtorKolokvium.Kolokviumski.Z1;

import com.sun.source.tree.Tree;

import java.util.*;

class Participant {
    private final String city;
    private final String code;
    private final String name;
    private final int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition {
    private final Map<String, Set<Participant>> participants;

    public Audition() {
        participants = new HashMap<>();
    }


    public void addParticipant(String city, String code, String name, int age) {
        Participant p = new Participant(city, code, name, age);
        if (!participants.containsKey(city)) {
            participants.putIfAbsent(city, new HashSet<>());
        }
        participants.get(city).add(p);
    }

    public void listByCity(String city) {
        Set<Participant> p = new TreeSet<>(Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge)
                .thenComparing(Participant::getCode));
        p.addAll(participants.get(city));
        p.forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
