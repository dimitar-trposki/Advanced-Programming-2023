package Juni.VtorKolokvium.Kolokviumski.Z27;

import java.util.*;
import java.util.stream.Collectors;

class Names {
    Map<String, Integer> names;

    public Names() {
        this.names = new TreeMap<>();
    }

    public void addName(String name) {
        names.putIfAbsent(name, 0);
        names.computeIfPresent(name, (key, val) -> val + 1);
    }

    private int uniqueLetters(String word) {
        Set<Character> set = new HashSet<>();
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            set.add(word.charAt(i));
        }
        return set.size();
    }

    public void printN(int n) {
        names.entrySet().stream()
                //.sorted(Map.Entry.comparingByKey())
                .filter(v -> v.getValue() >= n)
                .forEach(e ->
                        System.out.printf("%s (%d) %d%n", e.getKey(), e.getValue(), uniqueLetters(e.getKey())));
    }

    public String findName(int len, int x) {
        List<String> newNames = names.keySet().stream()
                .filter(integer -> integer.length() < len)
                .collect(Collectors.toList());

//        while (newNames.size() < x) {
//            List<String> newNames1 = new ArrayList<>(newNames);
//            newNames = new ArrayList<>(names.size() * 2);
//            newNames.addAll(newNames1);
//            newNames.addAll(newNames.size(), newNames1);
//        }

        return newNames.get(x % newNames.size());
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}