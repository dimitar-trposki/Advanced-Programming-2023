package IspitSeptemvri.Kolokviumski.K27;

import java.util.*;
import java.util.stream.Collectors;

class Names {

    Map<String, Integer> names;

    public Names() {
        this.names = new TreeMap<>();
    }

    public void addName(String name) {
        names.putIfAbsent(name, 0);
        names.computeIfPresent(name, (k, v) -> v + 1);
    }

    private int uniqueLetters(String name) {
        HashSet<Character> letters = new HashSet<>();
        char[] nameLetters = name.toCharArray();
        for (char nameLetter : nameLetters) {
            letters.add(Character.toLowerCase(nameLetter));
        }
        return letters.size();
    }

    public void printN(int n) {
        names.entrySet().stream()
                .filter(entry -> entry.getValue() >= n)
                .forEach(entry -> System.out.println(String.format("%s (%d) %d", entry.getKey(), entry.getValue(), uniqueLetters(entry.getKey()))));
    }

    public String findName(int len, int x) {
        List<String> filteredNames = names.keySet().stream()
                .filter(name -> name.length() < len)
                .collect(Collectors.toList());

        return filteredNames.get(x % filteredNames.size());
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