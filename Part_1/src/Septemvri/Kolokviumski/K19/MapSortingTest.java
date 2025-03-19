package Septemvri.Kolokviumski.K19;

import java.util.*;
import java.util.stream.Collectors;

public class MapSortingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> l = readMapPairs(scanner);
        if (n == 1) {
            Map<String, Integer> map = new HashMap<>();
            fillStringIntegerMap(l, map);
            SortedSet<Map.Entry<String, Integer>> s = entriesSortedByValues(map);
            System.out.println(s);
        } else {
            Map<Integer, String> map = new HashMap<>();
            fillIntegerStringMap(l, map);
            SortedSet<Map.Entry<Integer, String>> s = entriesSortedByValues(map);
            System.out.println(s);
        }

    }

    private static List<String> readMapPairs(Scanner scanner) {
        String line = scanner.nextLine();
        String[] entries = line.split("\\s+");
        return Arrays.asList(entries);
    }

    static void fillStringIntegerMap(List<String> l, Map<String, Integer> map) {
        l.stream()
                .forEach(s -> map.put(s.substring(0, s.indexOf(':')), Integer.parseInt(s.substring(s.indexOf(':') + 1))));
    }

    static void fillIntegerStringMap(List<String> l, Map<Integer, String> map) {
        l.stream()
                .forEach(s -> map.put(Integer.parseInt(s.substring(0, s.indexOf(':'))), s.substring(s.indexOf(':') + 1)));
    }

    private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        Comparator<Map.Entry<K, V>> comparator = (e1, e2) -> {
            int value = e2.getValue().compareTo(e1.getValue());
            return value != 0 ? value : 1;
        };

        SortedSet<Map.Entry<K, V>> sortedSet = new TreeSet<>(comparator);
        sortedSet.addAll(map.entrySet());
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(map.entrySet().stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
        sb.append("}");
        System.out.println(sb.toString());
        return sortedSet;
    }

}